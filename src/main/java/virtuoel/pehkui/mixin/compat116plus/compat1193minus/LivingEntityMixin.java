package virtuoel.pehkui.mixin.compat116plus.compat1193minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.LivingEntity;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin
{
	@ModifyConstant(method = MixinConstants.UPDATE_LIMBS, constant = @Constant(floatValue = 4.0F), remap = false)
	private float pehkui$updateLimbs$limbDistance(float value)
	{
		return ScaleUtils.modifyLimbDistance(value, (LivingEntity) (Object) this);
	}
}
