package virtuoel.pehkui.mixin.compat116minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin
{
	@ModifyExpressionValue(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", at = @At(value = "CONSTANT", args = "floatValue=0.4F"))
	private float pehkui$damage$knockback(float value, DamageSource source, float amount)
	{
		final float scale = ScaleUtils.getKnockbackScale(source.getAttacker());
		
		return scale != 1.0F ? scale * value : value;
	}
	
	@ModifyExpressionValue(method = "knockback(Lnet/minecraft/entity/LivingEntity;)V", at = @At(value = "CONSTANT", args = "floatValue=0.5F"))
	private float pehkui$knockback$knockback(float value, LivingEntity target)
	{
		final float scale = ScaleUtils.getKnockbackScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
}
