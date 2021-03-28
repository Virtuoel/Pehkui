package virtuoel.pehkui.mixin.client.compat116.compat1162plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin
{
	@Inject(method = "onPlayerRespawn", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;afterSpawn()V"))
	private void onOnPlayerRespawn(PlayerRespawnS2CPacket packet, CallbackInfo info, RegistryKey<World> dimension, DimensionType dimensionType, ClientPlayerEntity oldPlayer, int id, String brand, ClientPlayerEntity newPlayer)
	{
		ScaleUtils.loadScaleOnRespawn(newPlayer, oldPlayer, packet.shouldKeepPlayerAttributes());
	}
}
