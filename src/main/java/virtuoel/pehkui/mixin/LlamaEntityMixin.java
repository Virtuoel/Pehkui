package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.passive.LlamaEntity;

@Mixin(LlamaEntity.class)
public abstract class LlamaEntityMixin extends LivingEntityMixin
{
	@ModifyConstant(method = "updatePassengerPosition", constant = @Constant(floatValue = 0.3F))
	private float updatePassengerPositionModifyOffset(float value)
	{
		final float scale = pehkui_getScaleData().getScale();
		
		if (scale != 1.0F)
		{
			return scale * value;
		}
		
		return value;
	}
}
