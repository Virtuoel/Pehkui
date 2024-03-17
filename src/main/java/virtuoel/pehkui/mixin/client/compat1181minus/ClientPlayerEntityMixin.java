package virtuoel.pehkui.mixin.client.compat1181minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin
{
	@ModifyExpressionValue(method = "sendMovementPackets", at = @At(value = "CONSTANT", args = "doubleValue=9.0E-4D"))
	private double pehkui$sendMovementPackets$minVelocity(double value)
	{
		final float scale = ScaleUtils.getMotionScale((Entity) (Object) this);
		
		return scale < 1.0F ? scale * scale * value : value;
	}
}
