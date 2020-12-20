package virtuoel.pehkui.api;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.SortedSet;

import org.jetbrains.annotations.Nullable;

import it.unimi.dsi.fastutil.objects.ObjectRBTreeSet;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class ScaleData
{
	public static final ScaleData IDENTITY = Builder.create().buildImmutable(1.0F);
	
	@Deprecated
	public static ScaleData of(Entity entity, ScaleType type)
	{
		return type.getScaleData(entity);
	}
	
	@Deprecated
	public static ScaleData of(Entity entity)
	{
		return of(entity, ScaleType.BASE);
	}
	
	protected float scale = 1.0F;
	protected float prevScale = 1.0F;
	protected float fromScale = 1.0F;
	protected float toScale = 1.0F;
	protected int scaleTicks = 0;
	protected int totalScaleTicks = 20;
	
	@Deprecated
	public boolean scaleModified = false;
	private boolean shouldSync = false;
	
	@Deprecated
	protected Optional<Runnable> changeListener = Optional.empty();
	
	protected final ScaleType scaleType;
	@Nullable
	protected final Entity entity;
	
	protected ScaleData(ScaleType scaleType, @Nullable Entity entity)
	{
		this.scaleType = scaleType;
		this.entity = entity;
		
		getBaseValueModifiers().addAll(getScaleType().getDefaultBaseValueModifiers());
	}
	
	@Deprecated
	public ScaleData(Optional<Runnable> changeListener)
	{
		this(ScaleType.INVALID, null);
		this.changeListener = changeListener;
	}
	
	public void tick()
	{
		final float currScale = getBaseScale();
		final float targetScale = getTargetScale();
		final int scaleTickDelay = getScaleTickDelay();
		
		if (currScale != targetScale)
		{
			this.prevScale = currScale;
			if (this.scaleTicks >= scaleTickDelay)
			{
				this.fromScale = targetScale;
				this.scaleTicks = 0;
				setBaseScale(targetScale);
			}
			else
			{
				this.scaleTicks++;
				final float nextScale = currScale + ((targetScale - this.fromScale) / (float) scaleTickDelay);
				setBaseScale(nextScale);
			}
		}
		else if (this.prevScale != currScale)
		{
			this.prevScale = currScale;
		}
	}
	
	public ScaleType getScaleType()
	{
		return this.scaleType;
	}
	
	@Nullable
	public Entity getEntity()
	{
		return this.entity;
	}
	
	private final SortedSet<ScaleModifier> baseValueModifiers = new ObjectRBTreeSet<>();
	
	public SortedSet<ScaleModifier> getBaseValueModifiers()
	{
		return baseValueModifiers;
	}
	
	public float getBaseScale()
	{
		return getBaseScale(1.0F);
	}
	
	public float getBaseScale(float delta)
	{
		return delta == 1.0F ? scale : MathHelper.lerp(delta, getPrevScale(), scale);
	}
	
	public float getScale()
	{
		return getScale(1.0F);
	}
	
	protected float computeScale(float value, Collection<ScaleModifier> modifiers, float delta)
	{
		for (final ScaleModifier m : modifiers)
		{
			value = m.modifyScale(this, value, delta);
		}
		
		return value;
	}
	
	public float getScale(float delta)
	{
		return computeScale(getBaseScale(delta), getBaseValueModifiers(), delta);
	}
	
	public void setScale(float scale)
	{
		setBaseScale(scale);
		setTargetScale(scale);
	}
	
	public void setBaseScale(float scale)
	{
		this.prevScale = getBaseScale();
		this.scale = scale;
		onUpdate();
	}
	
	public float getInitialScale()
	{
		return this.fromScale;
	}
	
	public float getTargetScale()
	{
		return this.toScale;
	}
	
	public void setTargetScale(float targetScale)
	{
		this.fromScale = getBaseScale();
		this.toScale = targetScale;
		this.scaleTicks = 0;
	}
	
	public int getScaleTickDelay()
	{
		return this.totalScaleTicks;
	}
	
	public void setScaleTickDelay(int ticks)
	{
		this.totalScaleTicks = ticks;
	}
	
	public float getPrevScale()
	{
		return this.prevScale;
	}
	
	public void markForSync(boolean sync)
	{
		final Entity e = getEntity();
		
		if (e != null && e.world != null && !e.world.isClient)
		{
			this.shouldSync = sync;
		}
	}
	
	@Deprecated
	public void markForSync()
	{
		this.scaleModified = true;
		markForSync(true);
	}
	
	public boolean shouldSync()
	{
		return this.shouldSync;
	}
	
	public void onUpdate()
	{
		markForSync(true);
		getScaleType().getScaleChangedEvent().invoker().onEvent(this);
	}
	
	public PacketByteBuf toPacketByteBuf(PacketByteBuf buffer)
	{
		final SortedSet<ScaleModifier> baseValueModifiers = getBaseValueModifiers();
		
		buffer.writeFloat(this.scale)
		.writeFloat(this.prevScale)
		.writeFloat(this.fromScale)
		.writeFloat(this.toScale)
		.writeInt(this.scaleTicks)
		.writeInt(this.totalScaleTicks)
		.writeInt(baseValueModifiers.size());
		
		for (final ScaleModifier modifier : baseValueModifiers)
		{
			buffer.writeIdentifier(ScaleRegistries.getId(ScaleRegistries.SCALE_MODIFIERS, modifier));
		}
		
		return buffer;
	}
	
	public static CompoundTag fromPacketByteBufToTag(PacketByteBuf buffer)
	{
		final CompoundTag scaleData = new CompoundTag();
		
		final float scale = buffer.readFloat();
		final float prevScale = buffer.readFloat();
		final float fromScale = buffer.readFloat();
		final float toScale = buffer.readFloat();
		final int scaleTicks = buffer.readInt();
		final int totalScaleTicks = buffer.readInt();
		
		scaleData.putFloat("scale", scale);
		scaleData.putFloat("previous", prevScale);
		scaleData.putFloat("initial", fromScale);
		scaleData.putFloat("target", toScale);
		scaleData.putInt("ticks", scaleTicks);
		scaleData.putInt("total_ticks", totalScaleTicks);
		
		final int baseModifierCount = buffer.readInt();
		
		if (baseModifierCount != 0)
		{
			final ListTag modifiers = new ListTag();
			
			for (int i = 0; i < baseModifierCount; i++)
			{
				modifiers.add(StringTag.of(buffer.readString()));
			}
			
			scaleData.put("baseValueModifiers", modifiers);
		}
		
		return scaleData;
	}
	
	public void fromTag(CompoundTag scaleData)
	{
		this.scale = scaleData.contains("scale") ? scaleData.getFloat("scale") : 1.0F;
		this.prevScale = scaleData.contains("previous") ? scaleData.getFloat("previous") : this.scale;
		this.fromScale = scaleData.contains("initial") ? scaleData.getFloat("initial") : this.scale;
		this.toScale = scaleData.contains("target") ? scaleData.getFloat("target") : this.scale;
		this.scaleTicks = scaleData.contains("ticks") ? scaleData.getInt("ticks") : 0;
		this.totalScaleTicks = scaleData.contains("total_ticks") ? scaleData.getInt("total_ticks") : 20;
		
		final SortedSet<ScaleModifier> baseValueModifiers = getBaseValueModifiers();
		
		baseValueModifiers.clear();
		
		baseValueModifiers.addAll(getScaleType().getDefaultBaseValueModifiers());
		
		if (scaleData.contains("baseValueModifiers"))
		{
			final ListTag modifiers = scaleData.getList("baseValueModifiers", NbtType.STRING);
			
			Identifier id;
			ScaleModifier modifier;
			for (int i = 0; i < modifiers.size(); i++)
			{
				id = Identifier.tryParse(modifiers.getString(i));
				modifier = ScaleRegistries.getEntry(ScaleRegistries.SCALE_MODIFIERS, id);
				
				if (modifier != null)
				{
					baseValueModifiers.add(modifier);
				}
			}
		}
		
		onUpdate();
	}
	
	public CompoundTag toTag(CompoundTag tag)
	{
		tag.putFloat("scale", this.getBaseScale());
		tag.putFloat("initial", this.getInitialScale());
		tag.putFloat("target", this.getTargetScale());
		tag.putInt("ticks", this.scaleTicks);
		tag.putInt("total_ticks", this.totalScaleTicks);
		
		final SortedSet<ScaleModifier> baseValueModifiers = getBaseValueModifiers();
		
		if (!baseValueModifiers.isEmpty())
		{
			final ListTag modifiers = new ListTag();
			
			for (ScaleModifier modifier : baseValueModifiers)
			{
				modifiers.add(StringTag.of(ScaleRegistries.getId(ScaleRegistries.SCALE_MODIFIERS, modifier).toString()));
			}
			
			tag.put("baseValueModifiers", modifiers);
		}
		
		return tag;
	}
	
	public void fromScale(ScaleData scaleData)
	{
		fromScale(scaleData, true);
	}
	
	public ScaleData fromScale(ScaleData scaleData, boolean notifyListener)
	{
		this.scale = scaleData.getBaseScale();
		this.prevScale = scaleData.prevScale;
		this.fromScale = scaleData.getInitialScale();
		this.toScale = scaleData.getTargetScale();
		this.scaleTicks = scaleData.scaleTicks;
		this.totalScaleTicks = scaleData.totalScaleTicks;
		
		if (notifyListener)
		{
			onUpdate();
		}
		
		return this;
	}
	
	public ScaleData averagedFromScales(ScaleData scaleData, ScaleData... scales)
	{
		float scale = scaleData.getBaseScale();
		float prevScale = scaleData.prevScale;
		float fromScale = scaleData.getInitialScale();
		float toScale = scaleData.getTargetScale();
		int scaleTicks = scaleData.scaleTicks;
		int totalScaleTicks = scaleData.totalScaleTicks;
		
		for (final ScaleData data : scales)
		{
			scale += data.getBaseScale();
			prevScale += data.prevScale;
			fromScale += data.getInitialScale();
			toScale += data.getTargetScale();
			scaleTicks += data.scaleTicks;
			totalScaleTicks += data.totalScaleTicks;
		}
		
		final float count = scales.length + 1;
		
		this.scale = scale / count;
		this.prevScale = prevScale / count;
		this.fromScale = fromScale / count;
		this.toScale = toScale / count;
		this.scaleTicks = Math.round(scaleTicks / count);
		this.totalScaleTicks = Math.round(totalScaleTicks / count);
		
		onUpdate();
		
		return this;
	}
	
	@Deprecated
	public ScaleData averagedFromScales(boolean notifyListener, ScaleData scaleData, ScaleData... scales)
	{
		return averagedFromScales(scaleData, scales);
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(fromScale, prevScale, scale, scaleTicks, toScale, totalScaleTicks);
	}
	
	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		
		if (!(obj instanceof ScaleData))
		{
			return false;
		}
		
		final ScaleData other = (ScaleData) obj;
		
		return Float.floatToIntBits(scale) == Float.floatToIntBits(other.scale) &&
			Float.floatToIntBits(prevScale) == Float.floatToIntBits(other.prevScale) &&
			Float.floatToIntBits(fromScale) == Float.floatToIntBits(other.fromScale) &&
			Float.floatToIntBits(toScale) == Float.floatToIntBits(other.toScale) &&
			scaleTicks == other.scaleTicks &&
			totalScaleTicks == other.totalScaleTicks &&
			Float.floatToIntBits(getScale()) == Float.floatToIntBits(other.getScale());
	}
	
	public static class Builder
	{
		private Entity entity = null;
		private ScaleType type = null;
		
		public static Builder create()
		{
			return new Builder();
		}
		
		private Builder()
		{
			
		}
		
		public Builder type(ScaleType type)
		{
			this.type = type;
			return this;
		}
		
		public Builder entity(@Nullable Entity entity)
		{
			this.entity = entity;
			return this;
		}
		
		public ImmutableScaleData buildImmutable(float value)
		{
			return new ImmutableScaleData(value, type == null ? ScaleType.INVALID : type, entity);
		}
		
		public ScaleData build()
		{
			return new ScaleData(type == null ? ScaleType.INVALID : type, entity);
		}
	}
	
	public static class ImmutableScaleData extends ScaleData
	{
		protected ImmutableScaleData(float scale, ScaleType scaleType, @Nullable Entity entity)
		{
			super(scaleType, entity);
			this.scale = scale;
			this.prevScale = scale;
			this.fromScale = scale;
			this.toScale = scale;
		}
		
		@Deprecated
		public ImmutableScaleData(float scale)
		{
			this(scale, ScaleType.INVALID, null);
		}
		
		@Override
		public void tick()
		{
			
		}
		
		@Override
		public float getScale(float delta)
		{
			return getBaseScale(delta);
		}
		
		@Override
		public void setBaseScale(float scale)
		{
			
		}
		
		@Override
		public void setTargetScale(float targetScale)
		{
			
		}
		
		@Override
		public void setScaleTickDelay(int ticks)
		{
			
		}
		
		@Override
		public void markForSync(boolean sync)
		{
			
		}
		
		@Override
		public void onUpdate()
		{
			
		}
		
		@Override
		public void fromTag(CompoundTag scaleData)
		{
			
		}
		
		@Override
		public ScaleData fromScale(ScaleData scaleData, boolean notifyListener)
		{
			return this;
		}
	}
}
