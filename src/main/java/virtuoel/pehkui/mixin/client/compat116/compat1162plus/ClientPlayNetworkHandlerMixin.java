package virtuoel.pehkui.mixin.client.compat116.compat1162plus;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleRenderUtils;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin
{
	@Dynamic
	@Inject(method = MixinConstants.ON_PLAYER_RESPAWN, at = @At(value = "INVOKE", target = MixinConstants.AFTER_SPAWN))
	private void pehkui$onPlayerRespawn(PlayerRespawnS2CPacket packet, CallbackInfo info, @Local(ordinal = 0) ClientPlayerEntity oldPlayer, @Local(ordinal = 1) ClientPlayerEntity newPlayer)
	{
		ScaleUtils.loadScaleOnRespawn(newPlayer, oldPlayer, ScaleRenderUtils.wasPlayerAlive(packet));
	}
}
