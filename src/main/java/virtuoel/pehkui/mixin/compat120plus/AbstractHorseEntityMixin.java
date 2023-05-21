package virtuoel.pehkui.mixin.compat120plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.HorseBaseEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(HorseBaseEntity.class)
public abstract class AbstractHorseEntityMixin
{
	@ModifyConstant(method = "updatePassengerPosition(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity$PositionUpdater;)V", constant = @Constant(floatValue = 0.7F))
	private float pehkui$updatePassengerPosition$horizontalOffset(float value)
	{
		final float scale = ScaleUtils.getBoundingBoxWidthScale((Entity) (Object) this);
		
		if (scale != 1.0F)
		{
			return scale * value;
		}
		
		return value;
	}
	
	@ModifyConstant(method = "updatePassengerPosition(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity$PositionUpdater;)V", constant = @Constant(floatValue = 0.15F))
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
