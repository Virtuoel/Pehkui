package virtuoel.pehkui.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.MathHelper;

public interface ResizableEntity
{
	void tickScale();
	
	float getScale();
	
	void setScale(float scale);
	
	float getInitialScale();
	
	float getTargetScale();
	
	void setTargetScale(float scale);
	
	int getScaleTickDelay();
	
	void setScaleTickDelay(int ticks);
	
	float getPrevScale();
	
	default float getScale(float delta)
	{
		final float scale = getScale();
		return delta == 1.0F ? scale : MathHelper.lerp(delta, getPrevScale(), scale);
	}
	
	void scheduleScaleUpdate();
	
	default void setTargetScaleAndUpdate(float scale)
	{
		setTargetScale(scale);
		scheduleScaleUpdate();
	}
	
	boolean shouldSyncScale();
	
	PacketByteBuf scaleToPacketByteBuf(PacketByteBuf buffer);
	
	void scaleFromCompoundTag(CompoundTag scaleData);
	
	@Deprecated
	void scaleFromPacketByteBuf(PacketByteBuf buffer);
}
