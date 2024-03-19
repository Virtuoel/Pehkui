package virtuoel.pehkui.mixin.reach;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ScreenHandler.class)
public class ScreenHandlerMixin
{
	@ModifyExpressionValue(method = "method_17696", at = @At(value = "CONSTANT", args = "doubleValue=64.0D"))
	private static double pehkui$canUse$distance(double value, Block block, PlayerEntity player)
	{
		final float scale = ScaleUtils.getBlockReachScale(player);
		return scale > 1.0F ? scale * scale * value : value;
	}
}
