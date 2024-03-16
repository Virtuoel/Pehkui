package virtuoel.pehkui.mixin.compat116plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.entity.passive.FoxEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(targets = "net.minecraft.entity.passive.FoxEntity$MateGoal")
public abstract class FoxEntityMateGoalMixin extends AnimalMateGoal
{
	private FoxEntityMateGoalMixin()
	{
		super(null, 0);
	}
	
	@Inject(method = "breed()V", at = @At(value = "INVOKE", shift = Shift.BEFORE, target = "Lnet/minecraft/server/world/ServerWorld;spawnEntityAndPassengers(Lnet/minecraft/entity/Entity;)V"))
	private void pehkui$breed(CallbackInfo info, @Local FoxEntity foxEntity)
	{
		ScaleUtils.loadAverageScales(foxEntity, this.animal, this.mate);
	}
}
