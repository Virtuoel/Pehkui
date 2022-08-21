package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

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
	
	@ModifyConstant(method = "getUnderWaterLocation", constant = @Constant(doubleValue = 0.001))
	private double pehkui$getUnderWaterLocation$offset(double value)
	{
		final float scale = ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this);
		
		return scale > 1.0F ? scale * value : value;
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
	
	@ModifyConstant(method = "checkBoatInWater", constant = @Constant(doubleValue = 0.001))
	private double pehkui$checkBoatInWater$offset(double value)
	{
		final float scale = ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this);
		
		return scale > 1.0F ? scale * value : value;
	}
}
