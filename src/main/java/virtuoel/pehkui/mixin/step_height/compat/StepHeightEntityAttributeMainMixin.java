package virtuoel.pehkui.mixin.step_height.compat;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.LivingEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Pseudo
@Mixin(targets = "dev.emi.stepheightentityattribute.StepHeightEntityAttributeMain", remap = false)
public class StepHeightEntityAttributeMainMixin
{
	@Inject(method = "getStepHeight", at = @At(value = "RETURN"), cancellable = true, remap = false)
	private static void getStepHeight(LivingEntity entity, CallbackInfoReturnable<Float> info)
	{
		final float scale = ScaleUtils.getStepHeightScale(entity);
		
		if (scale != 1.0F)
		{
			info.setReturnValue(scale * scale * info.getReturnValueF());
		}
	}
}
