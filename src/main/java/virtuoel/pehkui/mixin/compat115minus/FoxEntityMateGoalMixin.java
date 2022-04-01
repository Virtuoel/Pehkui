package virtuoel.pehkui.mixin.compat115minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.entity.passive.FoxEntity;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(targets = MixinConstants.FOX_ENTITY_MATE_GOAL, remap = false)
public abstract class FoxEntityMateGoalMixin extends AnimalMateGoal
{
	private FoxEntityMateGoalMixin()
	{
		super(null, 0);
	}
	
	@Inject(method = MixinConstants.BREED, locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", shift = Shift.BEFORE, target = MixinConstants.SPAWN_ENTITY, remap = false), remap = false)
	private void onBreed(CallbackInfo info, FoxEntity foxEntity)
	{
		ScaleUtils.loadAverageScales(foxEntity, this.animal, this.mate);
	}
}
