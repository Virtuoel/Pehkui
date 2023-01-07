package virtuoel.pehkui.mixin.reach;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ScreenHandler.class)
public class ScreenHandlerMixin
{
	@ModifyConstant(method = { "method_17696", "func_216960_a", "m_38913_" }, require = 0, expect = 0, constant = @Constant(doubleValue = 64.0D))
	private static double pehkui$canUse$distance(double value, Block block, PlayerEntity player)
	{
		final float scale = ScaleUtils.getBlockReachScale(player);
		return scale > 1.0F ? scale * scale * value : value;
	}
}
