package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.passive.HorseBaseEntity;

@Mixin(HorseBaseEntity.class)
public abstract class HorseBaseEntityMixin extends LivingEntityMixin
{
	@ModifyConstant(method = "updatePassengerPosition", constant = @Constant(floatValue = 0.7F))
	private float updatePassengerPositionModifyHorizontalOffset(float value)
	{
		final float scale = pehkui_getScaleData().getScale();
		
		if (scale != 1.0F)
		{
			return scale * value;
		}
		
		return value;
	}
	
	@ModifyConstant(method = "updatePassengerPosition", constant = @Constant(floatValue = 0.15F))
	private float updatePassengerPositionModifyVerticalOffset(float value)
	{
		final float scale = pehkui_getScaleData().getScale();
		
		if (scale != 1.0F)
		{
			return scale * value;
		}
		
		return value;
	}
}
