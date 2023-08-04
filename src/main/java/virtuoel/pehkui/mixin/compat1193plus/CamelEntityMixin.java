package virtuoel.pehkui.mixin.compat1193plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
// import net.minecraft.entity.passive.CamelEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(/*Camel*/Entity.class)
public class CamelEntityMixin
{
	// TODO 1.19.3
	@Inject(at = @At("RETURN"), method = "getDimensions", cancellable = true)
	private void pehkui$getDimensions(EntityPose pose, CallbackInfoReturnable<EntityDimensions> info)
	{
	//	if (pose == EntityPose.SITTING)
		{
			info.setReturnValue(info.getReturnValue().scaled(ScaleUtils.getBoundingBoxWidthScale((Entity) (Object) this), ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this)));
		}
	}
	
	@ModifyConstant(method = "getMountedHeightOffset", constant = @Constant(floatValue = 0.6F))
	private float pehkui$getMountedHeightOffset$adultOffset(float value)
	{
		final float scale = ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
	
	@ModifyConstant(method = "getMountedHeightOffset", constant = @Constant(floatValue = 0.35F))
	private float pehkui$getMountedHeightOffset$babyOffset(float value)
	{
		final float scale = ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
	
	@ModifyConstant(method = "method_45346", constant = @Constant(floatValue = 0.5F))
	private float pehkui$camelMountedHeightOffset$sittingFrontOffset(float value)
	{
		final float scale = ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
	
	@ModifyConstant(method = "method_45346", constant = @Constant(floatValue = 0.1F))
	private float pehkui$camelMountedHeightOffset$sittingBackOffset(float value)
	{
		final float scale = ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
	
	@ModifyConstant(method = "method_45346", constant = @Constant(floatValue = 0.6F))
	private float pehkui$camelMountedHeightOffset$standingFrontOffset(float value)
	{
		final float scale = ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
	
	@ModifyConstant(method = "method_45346", constant = @Constant(floatValue = 0.35F))
	private float pehkui$camelMountedHeightOffset$standingBackOffset(float value)
	{
		final float scale = ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
	
	@ModifyConstant(method = "method_45346", constant = @Constant(floatValue = 1.43F))
	private float pehkui$camelMountedHeightOffset$firstMultiplier(float value)
	{
		final float scale = ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
	
	@ModifyConstant(method = "method_45346", constant = @Constant(floatValue = 0.2F))
	private float pehkui$camelMountedHeightOffset$secondMultiplier(float value)
	{
		final float scale = ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
}
