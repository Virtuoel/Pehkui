package virtuoel.pehkui.mixin.compat116plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.DragonFireballEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(DragonFireballEntity.class)
public abstract class DragonFireballEntityMixin
{
	@ModifyExpressionValue(method = "onCollision(Lnet/minecraft/util/hit/HitResult;)V", at = @At(value = "CONSTANT", args = "doubleValue=4.0D"))
	private double pehkui$onCollision$width(double value)
	{
		final float scale = ScaleUtils.getBoundingBoxWidthScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
	
	@ModifyExpressionValue(method = "onCollision(Lnet/minecraft/util/hit/HitResult;)V", at = @At(value = "CONSTANT", args = "doubleValue=2.0D"))
	private double pehkui$onCollision$height(double value)
	{
		final float scale = ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
}
