package virtuoel.pehkui.mixin.reach.compat119plus;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin
{
	@Shadow ServerPlayerEntity player;
	/*
	@Redirect(method = "onPlayerInteractBlock", at = @At(value = "FIELD", opcode = Opcodes.GETSTATIC, target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;MAX_BREAK_SQUARED_DISTANCE:D"))
	private double pehkui$onPlayerInteractBlock$distance()
	{
		final float scale = ScaleUtils.getBlockReachScale(player);
		return scale <= 1.0F ? ServerPlayNetworkHandler.MAX_BREAK_SQUARED_DISTANCE : ServerPlayNetworkHandler.MAX_BREAK_SQUARED_DISTANCE * scale * scale;
	}
	
	@Redirect(method = "onPlayerInteractEntity", at = @At(value = "FIELD", opcode = Opcodes.GETSTATIC, target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;MAX_BREAK_SQUARED_DISTANCE:D"))
	private double pehkui$onPlayerInteractEntity$distance()
	{
		final float scale = ScaleUtils.getEntityReachScale(player);
		return scale <= 1.0F ? ServerPlayNetworkHandler.MAX_BREAK_SQUARED_DISTANCE : ServerPlayNetworkHandler.MAX_BREAK_SQUARED_DISTANCE * scale * scale;
	}
	*/
}
