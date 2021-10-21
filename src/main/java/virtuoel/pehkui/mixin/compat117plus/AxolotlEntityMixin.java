package virtuoel.pehkui.mixin.compat117plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AxolotlEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(AxolotlEntity.class)
public class AxolotlEntityMixin
{
	@ModifyConstant(method = "squaredAttackRange", constant = @Constant(doubleValue = 1.5D))
	private double squaredAttackRangeModifyRange(double value)
	{
		final float scale = ScaleUtils.getBoundingBoxWidthScale((Entity) (Object) this);
		
		if (scale != 1.0F)
		{
			return scale * scale * value;
		}
		
		return value;
	}
}
