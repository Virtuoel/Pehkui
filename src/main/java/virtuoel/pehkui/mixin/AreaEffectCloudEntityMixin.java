package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(AreaEffectCloudEntity.class)
public class AreaEffectCloudEntityMixin
{
	@ModifyExpressionValue(method = "tick", at = @At(value = "CONSTANT", args = "floatValue=0.5F"))
	private float pehkui$tick$minRadius(float value)
	{
		final float scale = ScaleUtils.getBoundingBoxWidthScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
	
	@ModifyExpressionValue(method = "getDimensions", at = @At(value = "CONSTANT", args = "floatValue=0.5F"))
	private float pehkui$getDimensions$height(float value)
	{
		final float scale = ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
}
