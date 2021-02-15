package virtuoel.pehkui.mixin.compat115plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import virtuoel.pehkui.mixin.LivingEntityMixin;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin
{
	@ModifyArg(method = "adjustMovementForSneaking", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;offset(DDD)Lnet/minecraft/util/math/Box;"))
	private double adjustMovementForSneakingStepHeightProxy(double stepHeight)
	{
		final float scale = ScaleUtils.getMotionScale((Entity) (Object) this);
		
		return scale != 1.0F ? stepHeight * scale : stepHeight;
	}
}
