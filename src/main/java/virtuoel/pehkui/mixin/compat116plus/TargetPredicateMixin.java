package virtuoel.pehkui.mixin.compat116plus;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(TargetPredicate.class)
public class TargetPredicateMixin
{
	@Shadow boolean useDistanceScalingFactor;
	
	@ModifyConstant(method = "test", constant = @Constant(doubleValue = 2.0D))
	private double testModifyMinDistance(double value, @Nullable LivingEntity baseEntity, LivingEntity targetEntity)
	{
		if (useDistanceScalingFactor)
		{
			final float scale = ScaleUtils.getVisibilityScale(targetEntity);
			
			return scale != 1.0F ? value * scale : value;
		}
		
		return value;
	}
}
