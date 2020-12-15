package virtuoel.pehkui.mixin.compat115minus;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(AnimalMateGoal.class)
public class AnimalMateGoalMixin
{
	@Shadow(remap = false)
	protected @Final @Mutable AnimalEntity field_6404;
	
	@Inject(method = "method_6249()V", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", shift = Shift.BEFORE, target = "Lnet/minecraft/class_1937;method_8649(Lnet/minecraft/class_1297;)Z", remap = false), remap = false)
	private void onBreed(CallbackInfo info, PassiveEntity passiveEntity)
	{
		ScaleUtils.loadScale(passiveEntity, field_6404);
	}
}
