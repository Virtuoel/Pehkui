package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.server.network.EntityTrackerEntry;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(EntityTrackerEntry.class)
public abstract class EntityTrackerEntryMixin
{
	@Shadow @Final Entity entity;
	@Shadow abstract void sendSyncPacket(Packet<?> packet);
	
	@Inject(at = @At("TAIL"), method = "tick")
	private void pehkui$tick(CallbackInfo info)
	{
		ScaleUtils.syncScalesIfNeeded(entity, this::sendSyncPacket);
	}
	
	@ModifyConstant(method = "tick", constant = @Constant(doubleValue = 7.62939453125E-6D))
	private double pehkui$tick$minimumSquaredDistance(double value)
	{
		final double scale = ScaleUtils.getMotionScale(entity);
		
		return scale < 1.0D ? value * scale * scale : value;
	}
	
	@Inject(at = @At("HEAD"), method = "syncEntityData")
	private void pehkui$syncEntityData(CallbackInfo info)
	{
		ScaleUtils.syncScalesIfNeeded(entity, this::sendSyncPacket);
	}
}
