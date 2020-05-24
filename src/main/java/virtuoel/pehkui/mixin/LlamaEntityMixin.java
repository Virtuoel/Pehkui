package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.LlamaEntity;
import virtuoel.pehkui.api.ScaleData;

@Mixin(LlamaEntity.class)
public class LlamaEntityMixin
{
	@ModifyConstant(method = "updatePassengerPosition", constant = @Constant(floatValue = 0.3F))
	private float updatePassengerPositionModifyOffset(float value)
	{
		final float scale = ScaleData.of((Entity) (Object) this).getScale();
		
		if (scale != 1.0F)
		{
			return scale * value;
		}
		
		return value;
	}
}
