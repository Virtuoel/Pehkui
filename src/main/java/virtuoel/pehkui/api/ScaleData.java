package virtuoel.pehkui.api;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.SortedSet;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import it.unimi.dsi.fastutil.objects.ObjectRBTreeSet;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class ScaleData
{
	public static final ScaleData IDENTITY = Builder.create().buildImmutable(1.0F);
	
	/**
	 * @see {@link ScaleType#getScaleData(Entity)}
	 */
	@Deprecated
	@ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
	public static ScaleData of(Entity entity, ScaleType type)
	{
		return type.getScaleData(entity);
	}
	
	/**
	 * @see {@link ScaleType#BASE}
	 * @see {@link ScaleType#getScaleData(Entity)}
	 */
	@Deprecated
	@ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
	public static ScaleData of(Entity entity)
	{
		return of(entity, ScaleType.BASE);
	}
	
	protected float scale;
	protected float prevScale;
	protected float fromScale;
	protected float toScale;
	protected int scaleTicks = 0;
	protected int totalScaleTicks;
	
	@Deprecated
	@ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
	public boolean scaleModified = false;
	private boolean shouldSync = false;
	
	@Deprecated
	@ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
	protected Optional<Runnable> changeListener = Optional.empty();
	
	protected final ScaleType scaleType;
	
	@Nullable
	protected final Entity entity;
	
	private final SortedSet<ScaleModifier> baseValueModifiers = new ObjectRBTreeSet<>();
	
	/**
	 * @see {@link ScaleType#getScaleData(Entity)}
	 * @see {@link ScaleData.Builder#create()}
	 */
	protected ScaleData(ScaleType scaleType, @Nullable Entity entity)
	{
		this.scaleType = scaleType;
		this.entity = entity;
		this.scale = scaleType.getDefaultBaseScale();
		this.prevScale = this.scale;
		this.fromScale = this.scale;
		this.toScale = this.scale;
		this.totalScaleTicks = scaleType.getDefaultTickDelay();
		
		getBaseValueModifiers().addAll(getScaleType().getDefaultBaseValueModifiers());
	}
	
	@Deprecated
	@ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
	public ScaleData(Optional<Runnable> changeListener)
	{
		this(ScaleType.INVALID, null);
		this.changeListener = changeListener;
	}
	
	/**
	 * Called at the start of {@link Entity#tick()}.
	 * <p>Pre and post tick callbacks are not invoked here. If calling this manually, be sure to invoke callbacks!
	 */
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
	
	/**
	 * Returns a mutable sorted set of scale modifiers. This set already contains the default modifiers from the scale type.
	 * @return Set of scale modifiers sorted by priority
	 */
	public SortedSet<ScaleModifier> getBaseValueModifiers()
	{
		return baseValueModifiers;
	}
	
	/**
	 * Returns the given scale value with modifiers applied from the given collection.
	 * 
	 * @param value The scale value to be modified.
	 * @param modifiers A sorted collection of scale modifiers to apply to the given value.
	 * @param delta Tick delta for use with rendering. Use 1.0F if no delta is available.
	 * @return Scale with modifiers applied
	 */
	protected float computeScale(float value, Collection<ScaleModifier> modifiers, float delta)
	{
		for (final ScaleModifier m : modifiers)
		{
			value = m.modifyScale(this, value, delta);
		}
		
		return value;
	}
	
	/**
	 * Gets the scale without any modifiers applied
	 * 
	 * @return Scale without any modifiers applied
	 */
	public float getBaseScale()
	{
		return getBaseScale(1.0F);
	}
	
	/**
	 * Gets the scale without any modifiers applied
	 * 
	 * @param delta Tick delta for use with rendering. Use 1.0F if no delta is available.
	 * @return Scale without any modifiers applied
	 */
	public float getBaseScale(float delta)
	{
		return delta == 1.0F ? scale : MathHelper.lerp(delta, getPrevScale(), scale);
	}
	
	/**
	 * Sets the scale to the given value, updates the previous scale, and notifies listeners
	 * 
	 * @param scale New scale value to be set
	 */
	public void setBaseScale(float scale)
	{
		this.prevScale = getBaseScale();
		this.scale = scale;
		onUpdate();
	}
	
	/**
	 * Gets the scale with modifiers applied
	 * 
	 * @return Scale with modifiers applied
	 */
	public float getScale()
	{
		return getScale(1.0F);
	}
	
	/**
	 * Gets the scale with modifiers applied
	 * 
	 * @param delta Tick delta for use with rendering. Use 1.0F if no delta is available.
	 * @return Scale with modifiers applied
	 */
	public float getScale(float delta)
	{
		return computeScale(getBaseScale(delta), getBaseValueModifiers(), delta);
	}
	
	/**
	 * Helper for instant resizing that sets both the base scale and target scale.
	 * 
	 * @param scale New scale value to be set
	 */
	public void setScale(float scale)
	{
		setBaseScale(scale);
		setTargetScale(scale);
	}
	
	public float getInitialScale()
	{
		return this.fromScale;
	}
	
	public float getTargetScale()
	{
		return this.toScale;
	}
	
	/**
	 * Sets a target scale. The base scale will be gradually changed to this over the amount of ticks specified by the scale tick delay.
	 * 
	 * @param targetScale The scale that the base scale should gradually change to
	 */
	public void setTargetScale(float targetScale)
	{
		this.fromScale = getBaseScale();
		this.toScale = targetScale;
		this.scaleTicks = 0;
		markForSync(true);
	}
	
	/**
	 * Gets the amount of ticks it will take for the base scale to change to the target scale
	 * 
	 * @return Delay in ticks
	 */
	public int getScaleTickDelay()
	{
		return this.totalScaleTicks;
	}
	
	/**
	 * Sets the amount of ticks it will take for the base scale to change to the target scale
	 * 
	 * @param ticks Delay in ticks
	 */
	public void setScaleTickDelay(int ticks)
	{
		this.totalScaleTicks = ticks;
		markForSync(true);
	}
	
	/**
	 * Gets the last value that the base scale was set to. Useful for linear interpolation.
	 * 
	 * @return Last value of the base scale
	 */
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
	
	/**
	 * @see {@link #markForSync(boolean)}
	 */
	@Deprecated
	@ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
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
	
	@Deprecated
	@ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
	public PacketByteBuf toPacketByteBuf(PacketByteBuf buffer)
	{
		return toPacket(buffer);
	}
	
	public PacketByteBuf toPacket(PacketByteBuf buffer)
	{
		final SortedSet<ScaleModifier> syncedModifiers = new ObjectRBTreeSet<>();
		
		syncedModifiers.addAll(getBaseValueModifiers());
		syncedModifiers.removeAll(getScaleType().getDefaultBaseValueModifiers());
		
		buffer.writeFloat(this.scale)
		.writeFloat(this.prevScale)
		.writeFloat(this.fromScale)
		.writeFloat(this.toScale)
		.writeInt(this.scaleTicks)
		.writeInt(this.totalScaleTicks)
		.writeInt(syncedModifiers.size());
		
		for (final ScaleModifier modifier : syncedModifiers)
		{
			buffer.writeIdentifier(ScaleRegistries.getId(ScaleRegistries.SCALE_MODIFIERS, modifier));
		}
		
		return buffer;
	}
	
	@Deprecated
	@ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
	public static NbtCompound fromPacketByteBufToTag(PacketByteBuf buffer)
	{
		return virtuoel.pehkui.util.ScaleUtils.buildScaleNbtFromPacketByteBuf(buffer);
	}
	
	@Deprecated
	@ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
	public void fromTag(NbtCompound tag)
	{
		readNbt(tag);
	}
	
	public void readNbt(NbtCompound tag)
	{
		final ScaleType type = getScaleType();
		
		this.scale = tag.contains("scale") ? tag.getFloat("scale") : type.getDefaultBaseScale();
		this.prevScale = tag.contains("previous") ? tag.getFloat("previous") : this.scale;
		this.fromScale = tag.contains("initial") ? tag.getFloat("initial") : this.scale;
		this.toScale = tag.contains("target") ? tag.getFloat("target") : this.scale;
		this.scaleTicks = tag.contains("ticks") ? tag.getInt("ticks") : 0;
		this.totalScaleTicks = tag.contains("total_ticks") ? tag.getInt("total_ticks") : type.getDefaultTickDelay();
		
		final SortedSet<ScaleModifier> baseValueModifiers = getBaseValueModifiers();
		
		baseValueModifiers.clear();
		
		baseValueModifiers.addAll(type.getDefaultBaseValueModifiers());
		
		if (tag.contains("baseValueModifiers"))
		{
			final NbtList modifiers = tag.getList("baseValueModifiers", NbtType.STRING);
			
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
	
	@Deprecated
	@ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
	public NbtCompound toTag(NbtCompound tag)
	{
		return writeNbt(tag);
	}
	
	public NbtCompound writeNbt(NbtCompound tag)
	{
		final ScaleType type = getScaleType();
		final float defaultBaseScale = type.getDefaultBaseScale();
		
		final float scale = getBaseScale();
		if (scale != defaultBaseScale)
		{
			tag.putFloat("scale", scale);
		}
		
		final float initial = getInitialScale();
		if (initial != defaultBaseScale)
		{
			tag.putFloat("initial", initial);
		}
		
		final float target = getTargetScale();
		if (target != defaultBaseScale)
		{
			tag.putFloat("target", target);
		}
		
		if (this.scaleTicks != 0)
		{
			tag.putInt("ticks", this.scaleTicks);
		}
		
		if (this.totalScaleTicks != type.getDefaultTickDelay())
		{
			tag.putInt("total_ticks", this.totalScaleTicks);
		}
		
		final SortedSet<ScaleModifier> savedModifiers = new ObjectRBTreeSet<>();
		
		savedModifiers.addAll(getBaseValueModifiers());
		savedModifiers.removeAll(getScaleType().getDefaultBaseValueModifiers());
		
		if (!savedModifiers.isEmpty())
		{
			final NbtList modifiers = new NbtList();
			
			for (ScaleModifier modifier : savedModifiers)
			{
				modifiers.add(NbtOps.INSTANCE.createString(ScaleRegistries.getId(ScaleRegistries.SCALE_MODIFIERS, modifier).toString()));
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
	
	/**
	 * Averages the values of the given scale data and sets its own values from them.
	 * 
	 * @param scaleData Single scale data
	 * @param scales Any additional scale data
	 * @return Itself
	 */
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
	@ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
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
		@ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
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
		
		@Deprecated
		@ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
		@Override
		public void fromTag(NbtCompound tag)
		{
			
		}
		
		@Override
		public void readNbt(NbtCompound tag)
		{
			
		}
		
		@Override
		public ScaleData fromScale(ScaleData scaleData, boolean notifyListener)
		{
			return this;
		}
	}
}
