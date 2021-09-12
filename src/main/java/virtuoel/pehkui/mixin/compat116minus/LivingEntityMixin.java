package virtuoel.pehkui.mixin.compat116minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin
{
	@ModifyConstant(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", constant = @Constant(floatValue = 0.4F))
	private float onDamageModifyKnockback(float value, DamageSource source, float amount)
	{
		final float scale = ScaleUtils.getKnockbackScale(source.getAttacker());
		
		return scale != 1.0F ? scale * value : value;
	}
	
	@ModifyConstant(method = "knockback(Lnet/minecraft/entity/LivingEntity;)V", constant = @Constant(floatValue = 0.5F))
	private float onKnockbackModifyKnockback(float value, LivingEntity target)
	{
		final float scale = ScaleUtils.getKnockbackScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
}
