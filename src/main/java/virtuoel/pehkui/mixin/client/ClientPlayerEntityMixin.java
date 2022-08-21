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
	@ModifyConstant(method = "tickMovement", constant = @Constant(floatValue = 3.0F))
	private float pehkui$tickMovement$flightSpeed(float value)
	{
		final float scale = ScaleUtils.getFlightScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
	
	@ModifyConstant(method = "autoJump", constant = { @Constant(floatValue = 1.2F), @Constant(floatValue = 0.75F) })
	private float pehkui$autoJump$heightAndBoost(float value)
	{
		final float scale = ScaleUtils.getMotionScale((Entity) (Object) this);
		final float jumpScale = ScaleUtils.getJumpHeightScale((Entity) (Object) this);
		
		return scale != 1.0F || jumpScale != 1.0F ? scale * jumpScale * value : value;
	}
}
