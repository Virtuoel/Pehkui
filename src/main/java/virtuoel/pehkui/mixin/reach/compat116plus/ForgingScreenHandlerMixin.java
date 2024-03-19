package virtuoel.pehkui.mixin.reach.compat116plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ForgingScreenHandler;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ForgingScreenHandler.class)
public class ForgingScreenHandlerMixin
{
	@ModifyExpressionValue(method = "method_24924", at = @At(value = "CONSTANT", args = "doubleValue=64.0D"))
	private double pehkui$canUse$distance(double value, PlayerEntity player)
	{
		final float scale = ScaleUtils.getBlockReachScale(player);
		return scale != 1.0F ? scale * scale * value : value;
	}
}
