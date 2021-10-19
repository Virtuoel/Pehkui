package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(TrackTargetGoal.class)
public class TrackTargetGoalMixin
{
	@Shadow LivingEntity target;
	@Shadow @Final @Mutable MobEntity mob;
	
	@Inject(method = "getFollowRange", at = @At("RETURN"), cancellable = true)
	private void onGetFollowRange(CallbackInfoReturnable<Double> info)
	{
		LivingEntity target = this.mob.getTarget();
		if (target == null && (target = this.target) == null)
		{
			return;
		}
		
		final float scale = ScaleUtils.getVisibilityScale(target);
		
		if (scale != 1.0F)
		{
			info.setReturnValue(info.getReturnValueD() * scale);
		}
	}
}
