package virtuoel.pehkui.mixin.compat117plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.entity.Entity;
import virtuoel.pehkui.api.PehkuiConfig;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(Entity.class)
public class EntityMixin
{
	@ModifyArg(method = "fall", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onLandedUpon(Lnet/minecraft/world/World;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;F)V"))
	private float pehkui$fall$fallDistance(float distance)
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
	private double pehkui$move$flapping(double value)
	{
		final float scale = ScaleUtils.getMotionScale((Entity) (Object) this);
		
		if (scale != 1.0F)
		{
			return value / scale;
		}
		
		return value;
	}
	
	@ModifyExpressionValue(method = "move", at = @At(value = "CONSTANT", ordinal = 0, args = "floatValue=0.6F"))
	private float pehkui$move$bobbing(float value)
	{
		final float scale = ScaleUtils.getMotionScale((Entity) (Object) this);
		
		if (scale != 1.0F)
		{
			return value / scale;
		}
		
		return value;
	}
	
	@ModifyExpressionValue(method = "move", at = @At(value = "CONSTANT", ordinal = 1, args = "floatValue=0.6F"))
	private float pehkui$move$step(float value)
	{
		final float scale = ScaleUtils.getMotionScale((Entity) (Object) this);
		
		if (scale != 1.0F)
		{
			return value / scale;
		}
		
		return value;
	}
}
