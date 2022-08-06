package virtuoel.pehkui.mixin.compat118minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import virtuoel.pehkui.util.GravityChangerCompatibility;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin
{
	@Shadow ServerPlayerEntity player;
	
	@ModifyConstant(method = MixinConstants.PROCESS_BLOCK_BREAKING_ACTION, constant = @Constant(doubleValue = 1.5D), remap = false)
	private double processBlockBreakingActionModifyDistance(double value)
	{
		return 0;
	}
	
	@ModifyConstant(method = MixinConstants.PROCESS_BLOCK_BREAKING_ACTION, constant = @Constant(doubleValue = 0.5D, ordinal = 0), remap = false)
	private double processBlockBreakingActionModifyXOffset(double value, BlockPos pos, PlayerActionC2SPacket.Action action, Direction direction, int worldHeight)
	{
		return ScaleUtils.getBlockXOffset(pos, player) + GravityChangerCompatibility.INSTANCE.getXCorrection(player);
	}
	
	@ModifyConstant(method = MixinConstants.PROCESS_BLOCK_BREAKING_ACTION, constant = @Constant(doubleValue = 0.5D, ordinal = 1), remap = false)
	private double processBlockBreakingActionModifyYOffset(double value, BlockPos pos, PlayerActionC2SPacket.Action action, Direction direction, int worldHeight)
	{
		return ScaleUtils.getBlockYOffset(pos, player) + GravityChangerCompatibility.INSTANCE.getYCorrection(player);
	}
	
	@ModifyConstant(method = MixinConstants.PROCESS_BLOCK_BREAKING_ACTION, constant = @Constant(doubleValue = 0.5D, ordinal = 2), remap = false)
	private double processBlockBreakingActionModifyZOffset(double value, BlockPos pos, PlayerActionC2SPacket.Action action, Direction direction, int worldHeight)
	{
		return ScaleUtils.getBlockZOffset(pos, player) + GravityChangerCompatibility.INSTANCE.getZCorrection(player);
	}
}
