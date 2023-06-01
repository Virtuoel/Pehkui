package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(PotionEntity.class)
public class PotionEntityMixin
{
	@ModifyArg(method = "applyWater", index = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
	private double pehkui$applyWater$expand$x(double value)
	{
		return value * ScaleUtils.getBoundingBoxWidthScale((Entity) (Object) this);
	}
	
	@ModifyArg(method = "applyWater", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
	private double pehkui$applyWater$expand$y(double value)
	{
		return value * ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this);
	}
	
	@ModifyArg(method = "applyWater", index = 2, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
	private double pehkui$applyWater$expand$z(double value)
	{
		return value * ScaleUtils.getBoundingBoxWidthScale((Entity) (Object) this);
	}
	
	@ModifyArg(method = "applySplashPotion", index = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
	private double pehkui$applySplashPotion$expand$x(double value)
	{
		return value * ScaleUtils.getBoundingBoxWidthScale((Entity) (Object) this);
	}
	
	@ModifyArg(method = "applySplashPotion", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
	private double pehkui$applySplashPotion$expand$y(double value)
	{
		return value * ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this);
	}
	
	@ModifyArg(method = "applySplashPotion", index = 2, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
	private double pehkui$applySplashPotion$expand$z(double value)
	{
		return value * ScaleUtils.getBoundingBoxWidthScale((Entity) (Object) this);
	}
	
	@ModifyArg(method = "applyLingeringPotion", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/AreaEffectCloudEntity;setRadius(F)V"))
	private double pehkui$applyLingeringPotion$setRadius(float value)
	{
		return value * ScaleUtils.getBoundingBoxWidthScale((Entity) (Object) this);
	}
	
	@ModifyArg(method = "applyLingeringPotion", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/AreaEffectCloudEntity;setRadiusOnUse(F)V"))
	private double pehkui$applyLingeringPotion$setRadiusOnUse(float value)
	{
		return value * ScaleUtils.getBoundingBoxWidthScale((Entity) (Object) this);
	}
}
