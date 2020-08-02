package virtuoel.pehkui.mixin.step_height;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.minecraft.entity.Entity;
import virtuoel.pehkui.api.PehkuiConfig;
import virtuoel.pehkui.api.ScaleData;

@Mixin(Entity.class)
public abstract class EntityMixin
{
	@Redirect(method = "adjustMovementForCollisions", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;stepHeight:F"))
	private float adjustMovementForCollisionsStepHeightProxy(Entity obj)
	{
		final float scale = ScaleData.of(obj).getScale();
		
		if (scale != 1.0F)
		{
			if (Optional.ofNullable(PehkuiConfig.DATA.get("scaledMotion"))
				.filter(JsonElement::isJsonPrimitive).map(JsonElement::getAsJsonPrimitive)
				.filter(JsonPrimitive::isBoolean).map(JsonPrimitive::getAsBoolean)
				.orElse(true))
			{
				return obj.stepHeight * scale;
			}
		}
		
		return obj.stepHeight;
	}
}
