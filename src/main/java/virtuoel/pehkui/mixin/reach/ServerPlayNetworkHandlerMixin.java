package virtuoel.pehkui.mixin.reach;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import virtuoel.pehkui.api.PehkuiConfig;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin
{
	@Shadow ServerPlayerEntity player;
	
	@ModifyConstant(method = "onPlayerInteractBlock", constant = @Constant(doubleValue = 64.0D))
	private double onPlayerInteractBlockModifyDistance(double value)
	{
		final float scale = ScaleUtils.getReachScale(player);
		
		if (scale > 1.0F)
		{
			if (Optional.ofNullable(PehkuiConfig.DATA.get("scaledReach"))
				.filter(JsonElement::isJsonPrimitive).map(JsonElement::getAsJsonPrimitive)
				.filter(JsonPrimitive::isBoolean).map(JsonPrimitive::getAsBoolean)
				.orElse(true))
			{
				return scale * scale * value;
			}
		}
		
		return value;
	}
	
	@ModifyConstant(method = "onPlayerInteractEntity", constant = @Constant(doubleValue = 36.0D))
	private double onPlayerInteractEntityModifyDistance(double value)
	{
		final float scale = ScaleUtils.getReachScale(player);
		
		if (scale > 1.0F)
		{
			if (Optional.ofNullable(PehkuiConfig.DATA.get("scaledReach"))
				.filter(JsonElement::isJsonPrimitive).map(JsonElement::getAsJsonPrimitive)
				.filter(JsonPrimitive::isBoolean).map(JsonPrimitive::getAsBoolean)
				.orElse(true))
			{
				return scale * scale * value;
			}
		}
		
		return value;
	}
}
