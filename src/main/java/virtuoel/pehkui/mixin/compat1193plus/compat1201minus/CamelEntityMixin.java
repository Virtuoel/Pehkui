package virtuoel.pehkui.mixin.compat1193plus.compat1201minus;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.entity.Entity;
// import net.minecraft.entity.passive.CamelEntity;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(/*Camel*/Entity.class) // TODO 1.19.3
public class CamelEntityMixin
{
	@Dynamic
	@ModifyExpressionValue(method = MixinConstants.GET_MOUNTED_HEIGHT_OFFSET, at = @At(value = "CONSTANT", args = "floatValue=0.6F"))
	private float pehkui$getMountedHeightOffset$adultOffset(float value)
	{
		final float scale = ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
	
	@Dynamic
	@ModifyExpressionValue(method = MixinConstants.GET_MOUNTED_HEIGHT_OFFSET, at = @At(value = "CONSTANT", args = "floatValue=0.35F"))
	private float pehkui$getMountedHeightOffset$babyOffset(float value)
	{
		final float scale = ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
	
	@Dynamic
	@ModifyExpressionValue(method = MixinConstants.GET_PASSENGER_ATTACHMENT_Y, at = @At(value = "CONSTANT", args = "floatValue=0.5F"))
	private float pehkui$camelMountedHeightOffset$sittingFrontOffset(float value)
	{
		final float scale = ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
	
	@Dynamic
	@ModifyExpressionValue(method = MixinConstants.GET_PASSENGER_ATTACHMENT_Y, at = @At(value = "CONSTANT", args = "floatValue=0.1F"))
	private float pehkui$camelMountedHeightOffset$sittingBackOffset(float value)
	{
		final float scale = ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
	
	@Dynamic
	@ModifyExpressionValue(method = MixinConstants.GET_PASSENGER_ATTACHMENT_Y, at = @At(value = "CONSTANT", args = "floatValue=0.6F"))
	private float pehkui$camelMountedHeightOffset$standingFrontOffset(float value)
	{
		final float scale = ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
	
	@Dynamic
	@ModifyExpressionValue(method = MixinConstants.GET_PASSENGER_ATTACHMENT_Y, at = @At(value = "CONSTANT", args = "floatValue=0.35F"))
	private float pehkui$camelMountedHeightOffset$standingBackOffset(float value)
	{
		final float scale = ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
	
	@Dynamic
	@ModifyExpressionValue(method = MixinConstants.GET_PASSENGER_ATTACHMENT_Y, at = @At(value = "CONSTANT", args = "floatValue=1.43F"))
	private float pehkui$camelMountedHeightOffset$firstMultiplier(float value)
	{
		final float scale = ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
	
	@Dynamic
	@ModifyExpressionValue(method = MixinConstants.GET_PASSENGER_ATTACHMENT_Y, at = @At(value = "CONSTANT", args = "floatValue=0.2F"))
	private float pehkui$camelMountedHeightOffset$secondMultiplier(float value)
	{
		final float scale = ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
}
