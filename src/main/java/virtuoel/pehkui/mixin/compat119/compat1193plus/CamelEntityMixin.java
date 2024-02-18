package virtuoel.pehkui.mixin.compat119.compat1193plus;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.CamelEntity;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(CamelEntity.class)
public abstract class CamelEntityMixin
{
	@Dynamic
	@ModifyConstant(method = MixinConstants.UPDATE_PASSENGER_POSITION, constant = @Constant(floatValue = 0.5F))
	private float pehkui$updatePassengerPosition$frontOffset(float value, Entity passenger)
	{
		final float scale = ScaleUtils.getBoundingBoxWidthScale(passenger);
		
		return scale != 1.0F ? scale * value : value;
	}
	
	@Dynamic
	@ModifyConstant(method = MixinConstants.UPDATE_PASSENGER_POSITION, constant = @Constant(floatValue = -0.7F))
	private float pehkui$updatePassengerPosition$backOffset(float value, Entity passenger)
	{
		final float scale = ScaleUtils.getBoundingBoxWidthScale(passenger);
		
		return scale != 1.0F ? scale * value : value;
	}
	
	@Dynamic
	@ModifyConstant(method = MixinConstants.UPDATE_PASSENGER_POSITION, constant = @Constant(floatValue = 0.2F))
	private float pehkui$updatePassengerPosition$animalOffset(float value, Entity passenger)
	{
		final float scale = ScaleUtils.getBoundingBoxWidthScale(passenger);
		
		return scale != 1.0F ? scale * value : value;
	}
}
