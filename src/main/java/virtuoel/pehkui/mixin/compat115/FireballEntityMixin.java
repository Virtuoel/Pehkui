package virtuoel.pehkui.mixin.compat115;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.minecraft.entity.projectile.FireballEntity;
import virtuoel.pehkui.api.PehkuiConfig;
import virtuoel.pehkui.mixin.EntityMixin;

@Mixin(FireballEntity.class)
public abstract class FireballEntityMixin extends EntityMixin
{
	@ModifyArg(method = "method_7469", index = 4, at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1937;method_8537(Lnet/minecraft/class_1297;DDDFZLnet/minecraft/class_1927$class_4179;)Lnet/minecraft/class_1927;", remap = false), remap = false)
	private float onOnCollisionCreateExplosionProxy(float power)
	{
		if (Optional.ofNullable(PehkuiConfig.DATA.get("scaledExplosions"))
			.filter(JsonElement::isJsonPrimitive).map(JsonElement::getAsJsonPrimitive)
			.filter(JsonPrimitive::isBoolean).map(JsonPrimitive::getAsBoolean)
			.orElse(true))
		{
			return power * pehkui_getScaleData().getScale();
		}
		
		return power;
	}
}
