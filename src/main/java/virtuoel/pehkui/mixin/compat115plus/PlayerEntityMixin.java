package virtuoel.pehkui.mixin.compat115plus;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.minecraft.entity.player.PlayerEntity;
import virtuoel.pehkui.api.PehkuiConfig;
import virtuoel.pehkui.mixin.LivingEntityMixin;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin
{
	@ModifyArg(method = "adjustMovementForSneaking", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;offset(DDD)Lnet/minecraft/util/math/Box;"))
	private double adjustMovementForSneakingStepHeightProxy(double stepHeight)
	{
		if (Optional.ofNullable(PehkuiConfig.DATA.get("scaledMotion"))
			.filter(JsonElement::isJsonPrimitive).map(JsonElement::getAsJsonPrimitive)
			.filter(JsonPrimitive::isBoolean).map(JsonPrimitive::getAsBoolean)
			.orElse(true))
		{
			return stepHeight * pehkui_getScaleData().getScale();
		}
		
		return stepHeight;
	}
}
