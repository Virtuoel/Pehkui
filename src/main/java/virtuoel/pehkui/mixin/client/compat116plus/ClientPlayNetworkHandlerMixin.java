package virtuoel.pehkui.mixin.client.compat116plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionType;
import virtuoel.pehkui.api.ScaleData;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin
{
	@Inject(method = "onPlayerRespawn", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;afterSpawn()V"))
	private void onOnPlayerRespawn(PlayerRespawnS2CPacket packet, CallbackInfo info, RegistryKey<DimensionType> registryKey, DimensionType dimensionType, ClientPlayerEntity oldPlayer, int id, String brand, ClientPlayerEntity newPlayer)
	{
		if (packet.shouldKeepPlayerAttributes())
		{
			ScaleData.of(newPlayer).fromScale(ScaleData.of(oldPlayer));
		}
	}
}
