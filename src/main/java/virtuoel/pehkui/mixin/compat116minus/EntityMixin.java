package virtuoel.pehkui.mixin.compat116minus;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.entity.Entity;
import virtuoel.pehkui.api.PehkuiConfig;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(Entity.class)
public class EntityMixin
{
	@Dynamic
	@ModifyArg(method = MixinConstants.FALL, at = @At(value = "INVOKE", target = MixinConstants.ON_LANDED_UPON))
	private float pehkui$onFall$fallDistance(float distance)
	{
		final float scale = ScaleUtils.getFallingScale((Entity) (Object) this);
		
		if (scale != 1.0F)
		{
			if (PehkuiConfig.COMMON.scaledFallDamage.get())
			{
				return distance * scale;
			}
		}
		
		return distance;
	}
	
	@ModifyExpressionValue(method = "move", at = @At(value = "CONSTANT", ordinal = 0, args = "doubleValue=0.6D"))
	private double pehkui$move$bobbing(double value)
	{
		final float scale = ScaleUtils.getMotionScale((Entity) (Object) this);
		
		if (scale != 1.0F)
		{
			return value / scale;
		}
		
		return value;
	}
	
	@ModifyExpressionValue(method = "move", at = @At(value = "CONSTANT", ordinal = 1, args = "doubleValue=0.6D"))
	private double pehkui$move$step(double value)
	{
		final float scale = ScaleUtils.getMotionScale((Entity) (Object) this);
		
		if (scale != 1.0F)
		{
			return value / scale;
		}
		
		return value;
	}
}
