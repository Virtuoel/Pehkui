package virtuoel.pehkui.mixin.compat1201minus;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.util.MixinConstants;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin
{
	@Dynamic
	@Inject(method = MixinConstants.ON_PLAYER_CONNECT, at = @At(value = "RETURN"))
	private void pehkui$onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo info)
	{
		for (ScaleType type : ScaleRegistries.SCALE_TYPES.values())
		{
			type.getScaleData(player).markForSync(true);
		}
	}
}
