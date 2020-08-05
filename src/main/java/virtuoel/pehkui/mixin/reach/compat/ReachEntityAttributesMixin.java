package virtuoel.pehkui.mixin.reach.compat;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.LivingEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Pseudo
@Mixin(targets = "com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes", remap = false)
public class ReachEntityAttributesMixin
{
	@Inject(method = { "getReachDistance", "getAttackRange" }, at = @At(value = "RETURN"), cancellable = true, remap = false)
	private static void getDistance(LivingEntity entity, double value, CallbackInfoReturnable<Double> info)
	{
		final float scale = ScaleUtils.getReachScale(entity);
		
		if (scale > 1.0F)
		{
			info.setReturnValue(scale * info.getReturnValueD());
		}
	}
	
	@Inject(method = { "getSquaredReachDistance", "getSquaredAttackRange" }, at = @At(value = "RETURN"), cancellable = true, remap = false)
	private static void getSquaredDistance(LivingEntity entity, double value, CallbackInfoReturnable<Double> info)
	{
		final float scale = ScaleUtils.getReachScale(entity);
		
		if (scale > 1.0F)
		{
			info.setReturnValue(scale * scale * info.getReturnValueD());
		}
	}
}
