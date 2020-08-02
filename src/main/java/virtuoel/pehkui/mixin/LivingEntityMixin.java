package virtuoel.pehkui.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import virtuoel.pehkui.api.PehkuiConfig;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin
{
	@ModifyArg(method = "getEyeHeight", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getActiveEyeHeight(Lnet/minecraft/entity/EntityPose;Lnet/minecraft/entity/EntityDimensions;)F"))
	private EntityDimensions onGetEyeHeightDimensionsProxy(EntityDimensions dimensions)
	{
		return dimensions.scaled(1.0F / pehkui_getScaleData().getScale());
	}
	
	@ModifyConstant(method = "travel", constant = @Constant(floatValue = 1.0F, ordinal = 0))
	private float travelModifyFallDistance(float value)
	{
		final float scale = pehkui_getScaleData().getScale();
		
		if (scale != 1.0F)
		{
			if (Optional.ofNullable(PehkuiConfig.DATA.get("scaledFallDamage"))
				.filter(JsonElement::isJsonPrimitive).map(JsonElement::getAsJsonPrimitive)
				.filter(JsonPrimitive::isBoolean).map(JsonPrimitive::getAsBoolean)
				.orElse(true))
			{
				return value * scale;
			}
		}
		
		return value;
	}
	
	@Inject(method = "getEyeHeight", at = @At("RETURN"), cancellable = true)
	private void onGetEyeHeight(EntityPose pose, EntityDimensions dimensions, CallbackInfoReturnable<Float> info)
	{
		if (pose != EntityPose.SLEEPING)
		{
			final float scale = pehkui_getScaleData().getScale();
			
			if (scale != 1.0F)
			{
				info.setReturnValue(info.getReturnValueF() * scale);
			}
		}
	}
}
