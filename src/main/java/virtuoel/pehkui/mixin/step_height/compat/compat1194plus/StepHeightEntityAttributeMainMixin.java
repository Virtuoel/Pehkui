package virtuoel.pehkui.mixin.step_height.compat.compat1194plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.entity.LivingEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Pseudo
@Mixin(targets = "dev.emi.stepheightentityattribute.StepHeightEntityAttributeMain", remap = false)
public class StepHeightEntityAttributeMainMixin
{
	@ModifyReturnValue(method = "getStepHeight", at = @At("RETURN"), remap = false)
	private static float pehkui$getStepHeight(float original, float baseStepHeight, LivingEntity entity)
	{
		final float scale = ScaleUtils.getStepHeightScale(entity);
		
		return scale != 1.0F ? original * scale : original;
	}
}
