package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.MovementType;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin
{
	@Shadow ServerPlayerEntity player;
	
	@ModifyArg(method = "onVehicleMove", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;contract(D)Lnet/minecraft/util/math/Box;"))
	private double onVehicleMoveContractProxy(double value)
	{
		final float scale = ScaleUtils.getMotionScale(player);
		return scale < 1.0F ? value * scale : value;
	}
	
	@ModifyArg(method = "onVehicleMove", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V"))
	private Vec3d onVehicleMoveMoveProxy(MovementType type, Vec3d movement)
	{
		final float scale = ScaleUtils.getMotionScale(player.getRootVehicle());
		return scale != 1.0F ? movement.multiply(1.0F / scale) : movement;
	}
	
	@ModifyArg(method = "onPlayerMove", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V"))
	private Vec3d onPlayerMoveMoveProxy(MovementType type, Vec3d movement)
	{
		final float scale = ScaleUtils.getMotionScale(player);
		return scale != 1.0F ? movement.multiply(1.0F / scale) : movement;
	}
	
	@ModifyConstant(method = "onPlayerInteractBlock", constant = @Constant(doubleValue = 0.5D, ordinal = 0))
	private double onPlayerInteractBlockModifyXOffset(double value, PlayerInteractBlockC2SPacket packet)
	{
		final BlockHitResult hit = packet.getBlockHitResult();
		return ScaleUtils.getBlockXOffset(hit.getSide(), hit.getBlockPos(), player);
	}
	
	@ModifyConstant(method = "onPlayerInteractBlock", constant = @Constant(doubleValue = 0.5D, ordinal = 1))
	private double onPlayerInteractBlockModifyYOffset(double value, PlayerInteractBlockC2SPacket packet)
	{
		final BlockHitResult hit = packet.getBlockHitResult();
		return ScaleUtils.getBlockYOffset(hit.getSide(), hit.getBlockPos(), player) - player.getStandingEyeHeight();
	}
	
	@ModifyConstant(method = "onPlayerInteractBlock", constant = @Constant(doubleValue = 0.5D, ordinal = 2))
	private double onPlayerInteractBlockModifyZOffset(double value, PlayerInteractBlockC2SPacket packet)
	{
		final BlockHitResult hit = packet.getBlockHitResult();
		return ScaleUtils.getBlockZOffset(hit.getSide(), hit.getBlockPos(), player);
	}
}
