package virtuoel.pehkui.mixin.compat1194plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.entity.LivingEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin
{
	@ModifyArg(method = "updateLimbs(Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;updateLimbs(F)V"))
	private float pehkui$updateLimbs(float value)
	{
		return ScaleUtils.modifyLimbDistance(value, (LivingEntity) (Object) this);
	}
}
