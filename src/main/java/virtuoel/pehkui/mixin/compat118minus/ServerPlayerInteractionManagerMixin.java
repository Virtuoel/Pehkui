package virtuoel.pehkui.mixin.compat118minus;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

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
	
	@Dynamic
	@ModifyExpressionValue(method = MixinConstants.PROCESS_BLOCK_BREAKING_ACTION, at = @At(value = "CONSTANT", args = "doubleValue=1.5D"))
	private double pehkui$processBlockBreakingAction$distance(double value)
	{
		return 0;
	}
	
	@Dynamic
	@ModifyExpressionValue(method = MixinConstants.PROCESS_BLOCK_BREAKING_ACTION, at = @At(value = "CONSTANT", args = "doubleValue=0.5D", ordinal = 0))
	private double pehkui$processBlockBreakingAction$xOffset(double value, BlockPos pos, PlayerActionC2SPacket.Action action, Direction direction, int worldHeight)
	{
		return ScaleUtils.getBlockXOffset(pos, player) + GravityChangerCompatibility.INSTANCE.getXCorrection(player);
	}
	
	@Dynamic
	@ModifyExpressionValue(method = MixinConstants.PROCESS_BLOCK_BREAKING_ACTION, at = @At(value = "CONSTANT", args = "doubleValue=0.5D", ordinal = 1))
	private double pehkui$processBlockBreakingAction$yOffset(double value, BlockPos pos, PlayerActionC2SPacket.Action action, Direction direction, int worldHeight)
	{
		return ScaleUtils.getBlockYOffset(pos, player) + GravityChangerCompatibility.INSTANCE.getYCorrection(player);
	}
	
	@Dynamic
	@ModifyExpressionValue(method = MixinConstants.PROCESS_BLOCK_BREAKING_ACTION, at = @At(value = "CONSTANT", args = "doubleValue=0.5D", ordinal = 2))
	private double pehkui$processBlockBreakingAction$zOffset(double value, BlockPos pos, PlayerActionC2SPacket.Action action, Direction direction, int worldHeight)
	{
		return ScaleUtils.getBlockZOffset(pos, player) + GravityChangerCompatibility.INSTANCE.getZCorrection(player);
	}
}
