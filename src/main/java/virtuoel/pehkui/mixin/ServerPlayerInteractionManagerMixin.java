package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin
{
	@Shadow ServerPlayerEntity player;
	
	@ModifyConstant(method = "processBlockBreakingAction", constant = @Constant(doubleValue = 1.5D))
	private double processBlockBreakingActionModifyDistance(double value)
	{
		return 0;
	}
	
	@ModifyConstant(method = "processBlockBreakingAction", constant = @Constant(doubleValue = 0.5D, ordinal = 0))
	private double processBlockBreakingActionModifyXOffset(double value, BlockPos pos, PlayerActionC2SPacket.Action action, Direction direction, int worldHeight)
	{
		return ScaleUtils.getBlockXOffset(pos, player);
	}
	
	@ModifyConstant(method = "processBlockBreakingAction", constant = @Constant(doubleValue = 0.5D, ordinal = 1))
	private double processBlockBreakingActionModifyYOffset(double value, BlockPos pos, PlayerActionC2SPacket.Action action, Direction direction, int worldHeight)
	{
		return ScaleUtils.getBlockYOffset(pos, player);
	}
	
	@ModifyConstant(method = "processBlockBreakingAction", constant = @Constant(doubleValue = 0.5D, ordinal = 2))
	private double processBlockBreakingActionModifyZOffset(double value, BlockPos pos, PlayerActionC2SPacket.Action action, Direction direction, int worldHeight)
	{
		return ScaleUtils.getBlockZOffset(pos, player);
	}
}
