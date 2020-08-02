package virtuoel.pehkui.mixin.reach;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import virtuoel.pehkui.api.PehkuiConfig;
import virtuoel.pehkui.api.ScaleData;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin
{
	@Shadow ServerPlayerEntity player;
	
	@ModifyConstant(method = "processBlockBreakingAction", constant = @Constant(doubleValue = 36.0D))
	private double processBlockBreakingActionModifyDistance(double value)
	{
		final float scale = ScaleData.of(player).getScale();
		
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
