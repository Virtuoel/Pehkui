package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(value = AbstractHorseEntity.class, priority = 1050)
public abstract class HorseBaseEntityMixin
{
	@ModifyConstant(method = "updatePassengerPosition", constant = @Constant(floatValue = 0.7F))
	private float pehkui$updatePassengerPosition$horizontalOffset(float value)
	{
		final float scale = ScaleUtils.getBoundingBoxWidthScale((Entity) (Object) this);
		
		if (scale != 1.0F)
		{
			return scale * value;
		}
		
		return value;
	}
	
	@ModifyConstant(method = "updatePassengerPosition", constant = @Constant(floatValue = 0.15F))
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
