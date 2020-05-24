package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.HorseBaseEntity;
import virtuoel.pehkui.api.ScaleData;

@Mixin(HorseBaseEntity.class)
public class HorseBaseEntityMixin
{
	@ModifyConstant(method = "updatePassengerPosition", constant = @Constant(floatValue = 0.7F))
	private float updatePassengerPositionModifyHorizontalOffset(float value)
	{
		final float scale = ScaleData.of((Entity) (Object) this).getScale();
		
		if (scale != 1.0F)
		{
			return scale * value;
		}
		
		return value;
	}
	
	@ModifyConstant(method = "updatePassengerPosition", constant = @Constant(floatValue = 0.15F))
	private float updatePassengerPositionModifyVerticalOffset(float value)
	{
		final float scale = ScaleData.of((Entity) (Object) this).getScale();
		
		if (scale != 1.0F)
		{
			return scale * value;
		}
		
		return value;
	}
}
