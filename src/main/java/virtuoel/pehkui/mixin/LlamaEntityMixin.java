package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.LlamaEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(LlamaEntity.class)
public abstract class LlamaEntityMixin extends LivingEntityMixin
{
	@ModifyConstant(method = "updatePassengerPosition", constant = @Constant(floatValue = 0.3F))
	private float updatePassengerPositionModifyOffset(float value)
	{
		final float scale = ScaleUtils.getBoundingBoxWidthScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
}
