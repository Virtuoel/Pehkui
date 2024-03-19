package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.HorseScreenHandler;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(HorseScreenHandler.class)
public class HorseScreenHandlerMixin
{
	@ModifyExpressionValue(method = "canUse", at = @At(value = "CONSTANT", args = "floatValue=8.0F"))
	private float pehkui$canUse$distance(float value, @Local(argsOnly = true) PlayerEntity player)
	{
		final float scale = ScaleUtils.getEntityReachScale(player);
		
		return scale != 1.0F ? scale * value : value;
	}
}
