package virtuoel.pehkui.mixin.compat116plus;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(TargetPredicate.class)
public class TargetPredicateMixin
{
	@Shadow boolean useDistanceScalingFactor;
	
	@ModifyExpressionValue(method = "test", at = @At(value = "CONSTANT", args = "doubleValue=2.0D"))
	private double pehkui$test$minDistance(double value, @Nullable LivingEntity baseEntity, LivingEntity targetEntity)
	{
		if (useDistanceScalingFactor)
		{
			final float scale = ScaleUtils.getVisibilityScale(targetEntity);
			
			return scale != 1.0F ? value * scale : value;
		}
		
		return value;
	}
}
