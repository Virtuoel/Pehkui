package virtuoel.pehkui.mixin.compat115minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.entity.passive.FoxEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(targets = "net.minecraft.class_4019$class_4024", remap = false)
public abstract class FoxEntityMateGoalMixin extends AnimalMateGoal
{
	private FoxEntityMateGoalMixin()
	{
		super(null, 0);
	}
	
	@Inject(method = "method_6249()V", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", shift = Shift.BEFORE, target = "Lnet/minecraft/class_1937;method_8649(Lnet/minecraft/class_1297;)Z", remap = false), remap = false)
	private void onBreed(CallbackInfo info, FoxEntity foxEntity)
	{
		ScaleUtils.loadAverageScales(true, foxEntity, this.animal, this.mate);
	}
}
