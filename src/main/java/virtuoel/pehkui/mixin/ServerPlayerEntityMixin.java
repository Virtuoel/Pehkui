package virtuoel.pehkui.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.minecraft.server.network.ServerPlayerEntity;
import virtuoel.pehkui.api.PehkuiConfig;
import virtuoel.pehkui.entity.ResizableEntity;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends EntityMixin
{
	@Inject(at = @At("HEAD"), method = "copyFrom")
	private void onCopyFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo info)
	{
		alive |= Optional.ofNullable(PehkuiConfig.DATA.get("keepScaleOnRespawn"))
			.filter(JsonElement::isJsonPrimitive).map(JsonElement::getAsJsonPrimitive)
			.filter(JsonPrimitive::isBoolean).map(JsonPrimitive::getAsBoolean)
			.orElse(false);
		
		if (alive)
		{
			pehkui_getScaleData().fromScale(((ResizableEntity) oldPlayer).pehkui_getScaleData());
		}
	}
}
