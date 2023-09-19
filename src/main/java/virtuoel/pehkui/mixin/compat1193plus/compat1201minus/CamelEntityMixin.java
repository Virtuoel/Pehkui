package virtuoel.pehkui.mixin.compat1193plus.compat1201minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.CamelEntity;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(CamelEntity.class)
public class CamelEntityMixin
{
	@ModifyConstant(method = MixinConstants.GET_MOUNTED_HEIGHT_OFFSET, constant = @Constant(floatValue = 0.6F), remap = false)
	private float pehkui$getMountedHeightOffset$adultOffset(float value)
	{
		final float scale = ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
	
	@ModifyConstant(method = MixinConstants.GET_MOUNTED_HEIGHT_OFFSET, constant = @Constant(floatValue = 0.35F), remap = false)
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
