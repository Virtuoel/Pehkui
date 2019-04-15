package virtuoel.pehkui.api;

import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.MathHelper;

public interface ResizableEntity
{
	float getScale();
	
	void setScale(float scale);
	
	float getInitialScale();
	
	float getTargetScale();
	
	void setTargetScale(float scale);
	
	int getScaleTickDelay();
	
	void setScaleTickDelay(int ticks);
	
	float getPrevScale();
	
	default float getScaleLerp(float delta)
	{
		return MathHelper.lerp(delta, getPrevScale(), getScale());
	}
	
	void scheduleScaleUpdate();
	
	default void setTargetScaleAndUpdate(float scale)
	{
		setTargetScale(scale);
		scheduleScaleUpdate();
	}
	
	boolean shouldSyncScale();
	
	PacketByteBuf scaleToPacketByteBuf(PacketByteBuf buffer);
	
	void scaleFromPacketByteBuf(PacketByteBuf buffer);
}
