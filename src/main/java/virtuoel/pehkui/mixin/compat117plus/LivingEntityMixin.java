package virtuoel.pehkui.mixin.compat117plus;

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
	@ModifyConstant(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", constant = @Constant(doubleValue = 0.4000000059604645D))
	private double onDamageModifyKnockback(double value, DamageSource source, float amount)
	{
		final float scale = ScaleUtils.getKnockbackScale(source.getAttacker());
		
		return scale != 1.0F ? scale * value : value;
	}
	
	@ModifyConstant(method = "knockback(Lnet/minecraft/entity/LivingEntity;)V", constant = @Constant(doubleValue = 0.5D))
	private double onKnockbackModifyKnockback(double value, LivingEntity target)
	{
		final float scale = ScaleUtils.getKnockbackScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
}
