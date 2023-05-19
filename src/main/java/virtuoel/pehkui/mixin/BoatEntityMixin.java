package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.BoatEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(BoatEntity.class)
public abstract class BoatEntityMixin
{
	@ModifyArg(method = "tick", index = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
	private double pehkui$tick$expand$x(double value)
	{
		return value * ScaleUtils.getBoundingBoxWidthScale((Entity) (Object) this);
	}
	
	@ModifyArg(method = "tick", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
	private double pehkui$tick$expand$y(double value)
	{
		return value * ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this);
	}
	
	@ModifyArg(method = "tick", index = 2, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
	private double pehkui$tick$expand$z(double value)
	{
		return value * ScaleUtils.getBoundingBoxWidthScale((Entity) (Object) this);
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
	
	@ModifyConstant(method = "updateVelocity", constant = @Constant(doubleValue = 0.06153846016296973))
	private double pehkui$updateVelocity$multiplier(double value)
	{
		final float scale = ScaleUtils.getMotionScale((Entity) (Object) this);
		
		return scale > 1.0F ? scale * value : value;
	}
	
	@ModifyConstant(method = "updateVelocity", constant = @Constant(doubleValue = -7.0E-4))
	private double pehkui$updateVelocity$sinking(double value)
	{
		final float scale = ScaleUtils.getMotionScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
}
