package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.damage.DamageSource;
import virtuoel.pehkui.util.ReflectionUtils;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(EnderDragonEntity.class)
public class EnderDragonEntityMixin
{
	@ModifyArg(method = "crystalDestroyed", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/boss/dragon/EnderDragonEntity;damagePart(Lnet/minecraft/entity/boss/dragon/EnderDragonPart;Lnet/minecraft/entity/damage/DamageSource;F)Z"))
	private float pehkui$crystalDestroyed$damagePart(EnderDragonPart part, DamageSource source, float amount)
	{
		final Entity attacker = ReflectionUtils.getAttacker(source);
		
		if (attacker != null)
		{
			final float scale = ScaleUtils.getAttackScale(attacker);
			
			if (scale != 1.0F)
			{
				return amount / scale;
			}
		}
		
		return amount;
	}
}
