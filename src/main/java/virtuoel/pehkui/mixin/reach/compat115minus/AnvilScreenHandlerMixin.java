package virtuoel.pehkui.mixin.reach.compat115minus;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.AnvilScreenHandler;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(AnvilScreenHandler.class)
public class AnvilScreenHandlerMixin
{
	@Dynamic
	@ModifyExpressionValue(method = MixinConstants.ANVIL_CAN_USE_LAMBDA, at = @At(value = "CONSTANT", args = "doubleValue=64.0D"))
	private static double pehkui$canUse$distance(double value, PlayerEntity player)
	{
		final float scale = ScaleUtils.getBlockReachScale(player);
		return scale != 1.0F ? scale * scale * value : value;
	}
}
