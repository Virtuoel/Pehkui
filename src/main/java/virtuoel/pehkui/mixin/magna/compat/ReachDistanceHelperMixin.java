package virtuoel.pehkui.mixin.magna.compat;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.entity.player.PlayerEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Pseudo
@Mixin(targets = "draylar.magna.api.reach.ReachDistanceHelper", remap = false)
public class ReachDistanceHelperMixin
{
	@ModifyReturnValue(method = "getReachDistance", at = @At(value = "RETURN", ordinal = 1), remap = false)
	private static double pehkui$getReachDistance(double original, PlayerEntity player)
	{
		final float scale = ScaleUtils.getBlockReachScale(player);
		
		return scale > 1.0F ? original * scale : original;
	}
}
