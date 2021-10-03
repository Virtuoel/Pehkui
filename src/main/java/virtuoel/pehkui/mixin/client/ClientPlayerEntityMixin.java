package virtuoel.pehkui.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin
{
	@ModifyConstant(method = "sendMovementPackets", constant = @Constant(doubleValue = 9.0E-4D))
	private double sendMovementPacketsModifyMinVelocity(double value)
	{
		final float scale = ScaleUtils.getMotionScale((Entity) (Object) this);
		
		return scale < 1.0F ? scale * scale * value : value;
	}
	
	@ModifyConstant(method = "tickMovement", constant = @Constant(floatValue = 3.0F))
	private float onTickMovementModifyFlightSpeed(float value)
	{
		final float scale = ScaleUtils.getFlightScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
}
