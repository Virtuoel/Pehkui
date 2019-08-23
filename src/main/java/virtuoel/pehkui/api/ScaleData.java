package virtuoel.pehkui.api;

import java.util.Optional;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.MathHelper;

public class ScaleData
{
	public static final ScaleData IDENTITY = new ImmutableScaleData(1.0F);
	
	public static ScaleData of(Entity entity)
	{
		return ((ResizableEntity) entity).pehkui_getScaleData();
	}
	
	private static final int DEFAULT_SCALING_TICK_TIME = 20;
	
	float scale = 1.0F;
	float prevScale = 1.0F;
	float fromScale = 1.0F;
	float toScale = 1.0F;
	int scaleTicks = 0;
	int totalScaleTicks = DEFAULT_SCALING_TICK_TIME;
	
	public boolean scaleModified = false;
	
	protected Optional<Runnable> calculateDimensions;
	
	public ScaleData(Optional<Runnable> calculateDimensions)
	{
		this.calculateDimensions = calculateDimensions;
	}
	
	public void tick()
	{
		final float currScale = getScale();
		if(currScale != getTargetScale())
		{
			this.prevScale = currScale;
			if(this.scaleTicks >= this.totalScaleTicks)
			{
				this.fromScale = this.toScale;
				this.scaleTicks = 0;
				setScale(this.toScale);
			}
			else
			{
				this.scaleTicks++;
				final float nextScale = this.scale + ((this.toScale - this.fromScale) / (float) this.totalScaleTicks);
				setScale(nextScale);
			}
		}
		else if(this.prevScale != currScale)
		{
			this.prevScale = currScale;
		}
	}
	
	public float getScale()
	{
		return this.scale;
	}
	
	public float getScale(float delta)
	{
		final float scale = getScale();
		return delta == 1.0F ? scale : MathHelper.lerp(delta, getPrevScale(), scale);
	}
	
	public void setScale(float scale)
	{
		this.prevScale = this.scale;
		this.scale = scale;
		calculateDimensions.ifPresent(Runnable::run);
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
		this.fromScale = this.scale;
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
	
	public void markForSync()
	{
		this.scaleModified = true;
	}
	
	public boolean shouldSync()
	{
		return this.scaleModified;
	}
	
	public PacketByteBuf toPacketByteBuf(PacketByteBuf buffer)
	{
		buffer.writeFloat(this.scale)
		.writeFloat(this.prevScale)
		.writeFloat(this.fromScale)
		.writeFloat(this.toScale)
		.writeInt(this.scaleTicks)
		.writeInt(this.totalScaleTicks);
		return buffer;
	}
	
	public void fromTag(CompoundTag scaleData)
	{
		this.scale = scaleData.containsKey("scale") ? scaleData.getFloat("scale") : 1.0F;
		this.prevScale = scaleData.containsKey("previous") ? scaleData.getFloat("previous") : this.scale;
		this.fromScale = scaleData.containsKey("initial") ? scaleData.getFloat("initial") : this.scale;
		this.toScale = scaleData.containsKey("target") ? scaleData.getFloat("target") : this.scale;
		this.scaleTicks = scaleData.containsKey("ticks") ? scaleData.getInt("ticks") : 0;
		this.totalScaleTicks = scaleData.containsKey("total_ticks") ? scaleData.getInt("total_ticks") : DEFAULT_SCALING_TICK_TIME;
		
		calculateDimensions.ifPresent(Runnable::run);
	}
	
	public CompoundTag toTag(CompoundTag tag)
	{
		tag.putFloat("scale", getScale());
		tag.putFloat("initial", getInitialScale());
		tag.putFloat("target", getTargetScale());
		tag.putInt("ticks", this.scaleTicks);
		tag.putInt("total_ticks", this.totalScaleTicks);
		return tag;
	}
	
	public static class ImmutableScaleData extends ScaleData
	{
		public ImmutableScaleData(float scale)
		{
			super(Optional.empty());
			this.scale = scale;
			this.prevScale = scale;
			this.fromScale = scale;
			this.toScale = scale;
		}
		
		@Override
		public void tick()
		{
			
		}
		
		@Override
		public void setScale(float scale)
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
		public void markForSync()
		{
			
		}
		
		@Override
		public void fromTag(CompoundTag scaleData)
		{
			
		}
	}
}
