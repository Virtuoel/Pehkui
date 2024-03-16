package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.WitherEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(WitherEntity.class)
public class WitherEntityMixin
{
	@ModifyExpressionValue(method = "getHeadX", at = @At(value = "CONSTANT", args = "doubleValue=1.3D"))
	private double pehkui$getHeadX$offset(double value)
	{
		final float scale = ScaleUtils.getBoundingBoxWidthScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
	
	@ModifyExpressionValue(method = "getHeadZ", at = @At(value = "CONSTANT", args = "doubleValue=1.3D"))
	private double pehkui$getHeadZ$offset(double value)
	{
		final float scale = ScaleUtils.getBoundingBoxWidthScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
	
	@ModifyExpressionValue(method = "getHeadY", at = { @At(value = "CONSTANT", args = "doubleValue=3.0D"), @At(value = "CONSTANT", args = "doubleValue=2.2D") })
	private double pehkui$getHeadY$offset(double value)
	{
		final float scale = ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
}
