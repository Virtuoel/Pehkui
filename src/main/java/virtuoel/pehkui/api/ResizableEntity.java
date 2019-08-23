package virtuoel.pehkui.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.PacketByteBuf;

@Deprecated
public interface ResizableEntity
{
	ScaleData pehkui_getScaleData();
	
	@Deprecated
	default void tickScale()
	{
		pehkui_getScaleData().tick();
	}
	
	@Deprecated
	default float getScale()
	{
		return pehkui_getScaleData().getScale();
	}
	
	@Deprecated
	default void setScale(float scale)
	{
		pehkui_getScaleData().setScale(scale);
	}
	
	@Deprecated
	default float getInitialScale()
	{
		return pehkui_getScaleData().getInitialScale();
	}
	
	@Deprecated
	default float getTargetScale()
	{
		return pehkui_getScaleData().getTargetScale();
	}
	
	@Deprecated
	default void setTargetScale(float scale)
	{
		pehkui_getScaleData().setTargetScale(scale);
	}
	
	@Deprecated
	default int getScaleTickDelay()
	{
		return pehkui_getScaleData().getScaleTickDelay();
	}
	
	@Deprecated
	default void setScaleTickDelay(int ticks)
	{
		pehkui_getScaleData().setScaleTickDelay(ticks);
	}
	
	@Deprecated
	default float getPrevScale()
	{
		return pehkui_getScaleData().getPrevScale();
	}
	
	@Deprecated
	default float getScale(float delta)
	{
		return pehkui_getScaleData().getScale(delta);
	}
	
	@Deprecated
	default void scheduleScaleUpdate()
	{
		pehkui_getScaleData().scaleModified = true;
	}
	
	@Deprecated
	default void setTargetScaleAndUpdate(float scale)
	{
		pehkui_getScaleData().setTargetScale(scale);
		scheduleScaleUpdate();
	}
	
	@Deprecated
	default boolean shouldSyncScale()
	{
		return pehkui_getScaleData().scaleModified;
	}
	
	@Deprecated
	default PacketByteBuf scaleToPacketByteBuf(PacketByteBuf buffer)
	{
		return pehkui_getScaleData().toPacketByteBuf(buffer);
	}
	
	@Deprecated
	default void scaleFromCompoundTag(CompoundTag scaleData)
	{
		pehkui_getScaleData().fromTag(scaleData);
	}
	
	@Deprecated
	default void scaleFromPacketByteBuf(PacketByteBuf buffer)
	{
		final float scale = buffer.readFloat();
		final float prevScale = buffer.readFloat();
		final float fromScale = buffer.readFloat();
		final float toScale = buffer.readFloat();
		final int scaleTicks = buffer.readInt();
		final int totalScaleTicks = buffer.readInt();
		
		final CompoundTag scaleData = new CompoundTag();
		
		scaleData.putFloat("scale", scale);
		scaleData.putFloat("previous", prevScale);
		scaleData.putFloat("initial", fromScale);
		scaleData.putFloat("target", toScale);
		scaleData.putInt("ticks", scaleTicks);
		scaleData.putInt("total_ticks", totalScaleTicks);
		
		pehkui_getScaleData().fromTag(scaleData);
	}
}
