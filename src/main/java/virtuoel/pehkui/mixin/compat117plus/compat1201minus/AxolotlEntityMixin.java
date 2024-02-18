package virtuoel.pehkui.mixin.compat117plus.compat1201minus;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AxolotlEntity;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(AxolotlEntity.class)
public class AxolotlEntityMixin
{
	@Dynamic
	@ModifyConstant(method = MixinConstants.SQUARED_ATTACK_RANGE, constant = @Constant(doubleValue = 1.5D))
	private double pehkui$squaredAttackRange$range(double value)
	{
		final float scale = ScaleUtils.getBoundingBoxWidthScale((Entity) (Object) this);
		
		if (scale != 1.0F)
		{
			return scale * scale * value;
		}
		
		return value;
	}
}
