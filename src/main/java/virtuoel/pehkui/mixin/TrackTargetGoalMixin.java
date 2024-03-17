package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(TrackTargetGoal.class)
public class TrackTargetGoalMixin
{
	@Shadow LivingEntity target;
	@Shadow @Final @Mutable MobEntity mob;
	
	@ModifyReturnValue(method = "getFollowRange", at = @At("RETURN"))
	private double pehkui$getFollowRange(double original)
	{
		LivingEntity target = this.mob.getTarget();
		if (target == null && (target = this.target) == null)
		{
			return original;
		}
		
		final float scale = ScaleUtils.getVisibilityScale(target);
		
		return scale != 1.0F ? original * scale : original;
	}
}
