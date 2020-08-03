package virtuoel.pehkui.mixin.compat116plus;

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
	@ModifyArg(method = "onCollision(Lnet/minecraft/util/hit/HitResult;)V", index = 4, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;createExplosion(Lnet/minecraft/entity/Entity;DDDFZLnet/minecraft/world/explosion/Explosion$DestructionType;)Lnet/minecraft/world/explosion/Explosion;"))
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
