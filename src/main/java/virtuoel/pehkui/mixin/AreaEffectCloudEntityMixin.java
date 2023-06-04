package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(AreaEffectCloudEntity.class)
public class AreaEffectCloudEntityMixin
{
	@ModifyConstant(method = "tick", constant = @Constant(floatValue = 0.5F))
	private float pehkui$tick$minRadius(float value)
	{
		final float scale = ScaleUtils.getBoundingBoxWidthScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
	
	@ModifyConstant(method = "getDimensions", constant = @Constant(floatValue = 0.5F))
	private float pehkui$getDimensions$height(float value)
	{
		final float scale = ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
}
