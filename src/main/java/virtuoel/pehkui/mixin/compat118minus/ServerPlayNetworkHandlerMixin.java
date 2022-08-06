package virtuoel.pehkui.mixin.compat118minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin
{
	@Shadow ServerPlayerEntity player;
	
	@ModifyConstant(method = "onPlayerInteractBlock", require = 0, constant = @Constant(doubleValue = 0.5D, ordinal = 0))
	private double onPlayerInteractBlockModifyXOffset(double value, PlayerInteractBlockC2SPacket packet)
	{
		return ScaleUtils.getBlockXOffset(packet.getBlockHitResult().getBlockPos(), player);
	}
	
	@ModifyConstant(method = "onPlayerInteractBlock", require = 0, constant = @Constant(doubleValue = 0.5D, ordinal = 1))
	private double onPlayerInteractBlockModifyYOffset(double value, PlayerInteractBlockC2SPacket packet)
	{
		return ScaleUtils.getBlockYOffset(packet.getBlockHitResult().getBlockPos(), player);
	}
	
	@ModifyConstant(method = "onPlayerInteractBlock", require = 0, constant = @Constant(doubleValue = 0.5D, ordinal = 2))
	private double onPlayerInteractBlockModifyZOffset(double value, PlayerInteractBlockC2SPacket packet)
	{
		return ScaleUtils.getBlockZOffset(packet.getBlockHitResult().getBlockPos(), player);
	}
}
