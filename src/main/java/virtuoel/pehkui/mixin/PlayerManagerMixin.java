package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin
{
	@Inject(method = "onPlayerConnect", at = @At(value = "RETURN"))
	private void onOnPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo info)
	{
		for (ScaleType type : ScaleType.REGISTRY.values())
		{
			ScaleData.of(player, type).markForSync();
		}
	}
}
