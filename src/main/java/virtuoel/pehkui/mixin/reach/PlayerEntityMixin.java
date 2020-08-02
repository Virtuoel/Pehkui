package virtuoel.pehkui.mixin.reach;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.minecraft.entity.player.PlayerEntity;
import virtuoel.pehkui.api.PehkuiConfig;
import virtuoel.pehkui.mixin.LivingEntityMixin;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin
{
	@ModifyConstant(method = "attack", constant = @Constant(doubleValue = 9.0D))
	private double attackModifyDistance(double value)
	{
		final float scale = pehkui_getScaleData().getScale();
		
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
