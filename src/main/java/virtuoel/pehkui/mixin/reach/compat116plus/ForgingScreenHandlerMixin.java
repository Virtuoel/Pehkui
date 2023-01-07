package virtuoel.pehkui.mixin.reach.compat116plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ForgingScreenHandler;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ForgingScreenHandler.class)
public class ForgingScreenHandlerMixin
{
	@ModifyConstant(method = { "method_24924", "func_234646_a", "m_39783_" }, require = 0, expect = 0, constant = @Constant(doubleValue = 64.0D))
	private double pehkui$canUse$distance(double value, PlayerEntity player)
	{
		final float scale = ScaleUtils.getBlockReachScale(player);
		return scale != 1.0F ? scale * scale * value : value;
	}
}
