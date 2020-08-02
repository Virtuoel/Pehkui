package virtuoel.pehkui.mixin.client;

import java.util.Optional;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import virtuoel.pehkui.api.PehkuiConfig;
import virtuoel.pehkui.api.ScaleData;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin
{
	@Shadow @Final MinecraftClient client;
	
	@Inject(at = @At("RETURN"), method = "getReachDistance", cancellable = true)
	private void onGetReachDistance(CallbackInfoReturnable<Float> info)
	{
		if (client.player != null)
		{
			final float scale = ScaleData.of(client.player).getScale();
			
			if (scale != 1.0F)
			{
				if (Optional.ofNullable(PehkuiConfig.DATA.get("scaledReach"))
					.filter(JsonElement::isJsonPrimitive).map(JsonElement::getAsJsonPrimitive)
					.filter(JsonPrimitive::isBoolean).map(JsonPrimitive::getAsBoolean)
					.orElse(true))
				{
					info.setReturnValue(info.getReturnValue() * scale);
				}
			}
		}
	}
}
