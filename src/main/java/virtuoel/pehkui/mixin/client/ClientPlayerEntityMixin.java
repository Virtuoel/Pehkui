package virtuoel.pehkui.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.client.network.ClientPlayerEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin
{
	@ModifyConstant(method = "sendMovementPackets", constant = @Constant(doubleValue = 9.0E-4D))
	private double sendMovementPacketsModifyMinVelocity(double value)
	{
		final float scale = ScaleUtils.getMotionScale(this);
		
		return scale < 1.0F ? scale * scale * value : value;
	}
}
