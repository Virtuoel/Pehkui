package virtuoel.pehkui.mixin.reach.compat;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.minecraft.entity.LivingEntity;
import virtuoel.pehkui.api.PehkuiConfig;
import virtuoel.pehkui.api.ScaleData;

@Pseudo
@Mixin(targets = "com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes", remap = false)
public class ReachEntityAttributesMixin
{
	@Inject(method = { "getReachDistance", "getAttackRange" }, at = @At(value = "RETURN"), cancellable = true, remap = false)
	private static void getDistance(LivingEntity entity, double value, CallbackInfoReturnable<Double> info)
	{
		final float scale = ScaleData.of(entity).getScale();
		
		if (scale > 1.0F)
		{
			if (Optional.ofNullable(PehkuiConfig.DATA.get("scaledReach"))
				.filter(JsonElement::isJsonPrimitive).map(JsonElement::getAsJsonPrimitive)
				.filter(JsonPrimitive::isBoolean).map(JsonPrimitive::getAsBoolean)
				.orElse(true))
			{
				info.setReturnValue(scale * info.getReturnValueD());
			}
		}
	}
	
	@Inject(method = { "getSquaredReachDistance", "getSquaredAttackRange" }, at = @At(value = "RETURN"), cancellable = true, remap = false)
	private static void getSquaredDistance(LivingEntity entity, double value, CallbackInfoReturnable<Double> info)
	{
		final float scale = ScaleData.of(entity).getScale();
		
		if (scale > 1.0F)
		{
			if (Optional.ofNullable(PehkuiConfig.DATA.get("scaledReach"))
				.filter(JsonElement::isJsonPrimitive).map(JsonElement::getAsJsonPrimitive)
				.filter(JsonPrimitive::isBoolean).map(JsonPrimitive::getAsBoolean)
				.orElse(true))
			{
				info.setReturnValue(scale * scale * info.getReturnValueD());
			}
		}
	}
}
