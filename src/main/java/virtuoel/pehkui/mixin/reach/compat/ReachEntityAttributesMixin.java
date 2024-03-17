package virtuoel.pehkui.mixin.reach.compat;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.entity.LivingEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Pseudo
@Mixin(targets = "com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes", remap = false)
public class ReachEntityAttributesMixin
{
	@ModifyReturnValue(method = "getReachDistance", at = @At("RETURN"), remap = false)
	private static double pehkui$getBlockDistance(double original, LivingEntity entity, double value)
	{
		final float scale = ScaleUtils.getBlockReachScale(entity);
		
		return scale != 1.0F ? original * scale : original;
	}
	
	@ModifyReturnValue(method = "getAttackRange", at = @At("RETURN"), remap = false)
	private static double pehkui$getEntityDistance(double original, LivingEntity entity, double value)
	{
		final float scale = ScaleUtils.getEntityReachScale(entity);
		
		return scale != 1.0F ? original * scale : original;
	}
}
