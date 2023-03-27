package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ScreenHandler.class)
public class ScreenHandlerMixin
{
	@ModifyConstant(method = "method_17696", constant = @Constant(doubleValue = 0.5D, ordinal = 0))
	private static double pehkui$canUse$xOffset(double value, Block block, PlayerEntity player, World world, BlockPos pos)
	{
		return ScaleUtils.getBlockXOffset(pos, player);
	}
	
	@ModifyConstant(method = "method_17696", constant = @Constant(doubleValue = 0.5D, ordinal = 1))
	private static double pehkui$canUse$yOffset(double value, Block block, PlayerEntity player, World world, BlockPos pos)
	{
		return ScaleUtils.getBlockYOffset(pos, player);
	}
	
	@ModifyConstant(method = "method_17696", constant = @Constant(doubleValue = 0.5D, ordinal = 2))
	private static double pehkui$canUse$zOffset(double value, Block block, PlayerEntity player, World world, BlockPos pos)
	{
		return ScaleUtils.getBlockZOffset(pos, player);
	}
}
