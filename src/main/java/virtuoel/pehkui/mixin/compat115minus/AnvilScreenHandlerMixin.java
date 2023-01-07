package virtuoel.pehkui.mixin.compat115minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(AnvilScreenHandler.class)
public class AnvilScreenHandlerMixin
{
	@ModifyConstant(method = MixinConstants.ANVIL_CAN_USE_LAMBDA, constant = @Constant(doubleValue = 0.5D, ordinal = 0))
	private static double pehkui$canUse$xOffset(double value, PlayerEntity player, World world, BlockPos pos)
	{
		return ScaleUtils.getBlockXOffset(pos, player);
	}
	
	@ModifyConstant(method = MixinConstants.ANVIL_CAN_USE_LAMBDA, constant = @Constant(doubleValue = 0.5D, ordinal = 1))
	private static double pehkui$canUse$yOffset(double value, PlayerEntity player, World world, BlockPos pos)
	{
		return ScaleUtils.getBlockYOffset(pos, player);
	}
	
	@ModifyConstant(method = MixinConstants.ANVIL_CAN_USE_LAMBDA, constant = @Constant(doubleValue = 0.5D, ordinal = 2))
	private static double pehkui$canUse$zOffset(double value, PlayerEntity player, World world, BlockPos pos)
	{
		return ScaleUtils.getBlockZOffset(pos, player);
	}
}
