package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.screen.HorseScreenHandler;
import virtuoel.pehkui.api.ScaleData;

@Mixin(HorseScreenHandler.class)
public class HorseScreenHandlerMixin
{
	@Shadow @Final @Mutable HorseBaseEntity entity;
	
	@ModifyConstant(method = "canUse", constant = @Constant(floatValue = 8.0F))
	private float canUseModifyDistance(float value)
	{
		final float scale = ScaleData.of(entity).getScale();
		
		if (scale != 1.0F)
		{
			return scale * value;
		}
		
		return value;
	}
}
