package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(EnderDragonEntity.class)
public class EnderDragonEntityMixin
{
	@ModifyArg(method = "crystalDestroyed", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/boss/dragon/EnderDragonEntity;damagePart(Lnet/minecraft/entity/boss/dragon/EnderDragonPart;Lnet/minecraft/entity/damage/DamageSource;F)Z"))
	private float crystalDestroyedModifyDamage(EnderDragonPart part, DamageSource source, float amount)
	{
		if (source instanceof EntityDamageSource)
		{
			final float scale = ScaleUtils.getAttackScale(((EntityDamageSource) source).getAttacker());
			
			if (scale != 1.0F)
			{
				return amount / scale;
			}
		}
		
		return amount;
	}
}
