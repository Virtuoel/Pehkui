package virtuoel.pehkui.mixin.compat116plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ForgingScreenHandler.class)
public class ForgingScreenHandlerMixin
{
	@ModifyExpressionValue(method = { "method_24924", "func_234646_a", "m_39783_" }, require = 0, expect = 0, at = @At(value = "CONSTANT", args = "doubleValue=0.5D", ordinal = 0))
	private double pehkui$canUse$xOffset(double value, PlayerEntity player, World world, BlockPos pos)
	{
		return ScaleUtils.getBlockXOffset(pos, player);
	}
	
	@ModifyExpressionValue(method = { "method_24924", "func_234646_a", "m_39783_" }, require = 0, expect = 0, at = @At(value = "CONSTANT", args = "doubleValue=0.5D", ordinal = 1))
	private double pehkui$canUse$yOffset(double value, PlayerEntity player, World world, BlockPos pos)
	{
		return ScaleUtils.getBlockYOffset(pos, player);
	}
	
	@ModifyExpressionValue(method = { "method_24924", "func_234646_a", "m_39783_" }, require = 0, expect = 0, at = @At(value = "CONSTANT", args = "doubleValue=0.5D", ordinal = 2))
	private double pehkui$canUse$zOffset(double value, PlayerEntity player, World world, BlockPos pos)
	{
		return ScaleUtils.getBlockZOffset(pos, player);
	}
}
