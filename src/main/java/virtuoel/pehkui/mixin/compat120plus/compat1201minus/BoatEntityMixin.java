package virtuoel.pehkui.mixin.compat120plus.compat1201minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.BoatEntity;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(BoatEntity.class)
public abstract class BoatEntityMixin
{
	@ModifyConstant(method = MixinConstants.UPDATE_PASSENGER_POSITION, constant = @Constant(floatValue = 0.2F), remap = false)
	private float pehkui$updatePassengerPosition$frontOffset(float value, Entity passenger)
	{
		final float scale = ScaleUtils.getBoundingBoxWidthScale(passenger);
		
		return scale != 1.0F ? scale * value : value;
	}
	
	@ModifyConstant(method = MixinConstants.UPDATE_PASSENGER_POSITION, constant = @Constant(floatValue = -0.6F), remap = false)
	private float pehkui$updatePassengerPosition$backOffset(float value, Entity passenger)
	{
		final float scale = ScaleUtils.getBoundingBoxWidthScale(passenger);
		
		return scale != 1.0F ? scale * value : value;
	}
}
