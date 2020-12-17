package virtuoel.pehkui.mixin;

import java.util.function.Consumer;

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
	private void onTick(CallbackInfo info)
	{
		ScaleUtils.syncScalesIfNeeded(entity, this::sendSyncPacket);
	}
	
	@Inject(at = @At("TAIL"), method = "sendPackets")
	private void onSendPackets(Consumer<Packet<?>> sender, CallbackInfo info)
	{
		ScaleUtils.syncScalesOnTrackingStart(entity, sender);
	}
	
	@ModifyConstant(method = "tick", constant = @Constant(doubleValue = 7.62939453125E-6D))
	private double tickModifyMinimumSquaredDistance(double value)
	{
		final double scale = ScaleUtils.getMotionScale(entity);
		
		return scale < 1.0D ? value * scale * scale : value;
	}
}
