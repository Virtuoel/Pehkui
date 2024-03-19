package virtuoel.pehkui.mixin.compat116plus.compat1193minus;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.entity.LivingEntity;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin
{
	@Dynamic
	@ModifyExpressionValue(method = MixinConstants.UPDATE_LIMBS, at = @At(value = "CONSTANT", args = "floatValue=4.0F"))
	private float pehkui$updateLimbs$limbDistance(float value)
	{
		return ScaleUtils.modifyLimbDistance(value, (LivingEntity) (Object) this);
	}
}
