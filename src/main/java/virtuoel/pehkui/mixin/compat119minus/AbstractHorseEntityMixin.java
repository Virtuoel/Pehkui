package virtuoel.pehkui.mixin.compat119minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.HorseBaseEntity;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(value = HorseBaseEntity.class, priority = 1050)
public abstract class AbstractHorseEntityMixin
{
	@ModifyConstant(method = MixinConstants.UPDATE_PASSENGER_POSITION, constant = @Constant(floatValue = 0.7F), remap = false)
	private float pehkui$updatePassengerPosition$horizontalOffset(float value)
	{
		final float scale = ScaleUtils.getBoundingBoxWidthScale((Entity) (Object) this);
		
		if (scale != 1.0F)
		{
			return scale * value;
		}
		
		return value;
	}
	
	@ModifyConstant(method = MixinConstants.UPDATE_PASSENGER_POSITION, constant = @Constant(floatValue = 0.15F), remap = false)
	private float pehkui$updatePassengerPosition$verticalOffset(float value)
	{
		final float scale = ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this);
		
		if (scale != 1.0F)
		{
			return scale * value;
		}
		
		return value;
	}
}
