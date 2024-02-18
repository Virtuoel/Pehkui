package virtuoel.pehkui.mixin.compat119minus;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.ChickenEntity;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ChickenEntity.class)
public abstract class ChickenEntityMixin
{
	@Dynamic
	@ModifyConstant(method = MixinConstants.UPDATE_PASSENGER_POSITION, constant = @Constant(floatValue = 0.1F))
	private float pehkui$updatePassengerPosition$offset(float value, Entity passenger)
	{
		final float scale = ScaleUtils.getBoundingBoxWidthScale(passenger);
		
		return scale != 1.0F ? scale * value : value;
	}
}
