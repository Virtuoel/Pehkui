package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.math.Box;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(BoatEntity.class)
public abstract class BoatEntityMixin
{
	@WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
	private Box pehkui$tick$expand(Box obj, double x, double y, double z, Operation<Box> original)
	{
		final float widthScale = ScaleUtils.getBoundingBoxWidthScale((Entity) (Object) this);
		final float heightScale = ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this);
		
		if (widthScale != 1.0F)
		{
			x *= widthScale;
			z *= widthScale;
		}
		
		if (heightScale != 1.0F)
		{
			y *= heightScale;
		}
		
		return original.call(obj, x, y, z);
	}
	
	@ModifyArg(method = "checkBoatInWater", at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/util/math/MathHelper;ceil(D)I"))
	private double pehkui$checkBoatInWater$offset(double value)
	{
		final Entity self = (Entity) (Object) this;
		final float scale = ScaleUtils.getBoundingBoxHeightScale(self);
		
		if (scale != 1.0F)
		{
			final double minY = self.getBoundingBox().minY;
			
			return minY + (scale * (value - minY));
		}
		
		return value;
	}
	
	@ModifyVariable(method = "getUnderWaterLocation", at = @At(value = "STORE"))
	private double pehkui$getUnderWaterLocation$offset(double value)
	{
		final Entity self = (Entity) (Object) this;
		final float scale = ScaleUtils.getBoundingBoxHeightScale(self);
		
		if (scale > 1.0F)
		{
			final double maxY = self.getBoundingBox().maxY;
			
			return maxY + (scale * (value - maxY));
		}
		
		return value;
	}
	
	@ModifyExpressionValue(method = "updateVelocity", at = @At(value = "CONSTANT", args = "doubleValue=0.06153846016296973D"))
	private double pehkui$updateVelocity$multiplier(double value)
	{
		final float scale = ScaleUtils.getMotionScale((Entity) (Object) this);
		
		return scale > 1.0F ? scale * value : value;
	}
	
	@ModifyExpressionValue(method = "updateVelocity", at = @At(value = "CONSTANT", args = "doubleValue=-7.0E-4D"))
	private double pehkui$updateVelocity$sinking(double value)
	{
		final float scale = ScaleUtils.getMotionScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
}
