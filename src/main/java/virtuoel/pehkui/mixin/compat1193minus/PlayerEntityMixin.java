package virtuoel.pehkui.mixin.compat1193minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import virtuoel.pehkui.util.ReflectionUtils;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin
{
	@Inject(method = "travel(Lnet/minecraft/util/math/Vec3d;)V", at = @At(value = "INVOKE", ordinal = 0, shift = Shift.BEFORE, target = "Lnet/minecraft/entity/LivingEntity;travel(Lnet/minecraft/util/math/Vec3d;)V"))
	private void pehkui$travel$flightSpeed(Vec3d movementInput, CallbackInfo info)
	{
		final PlayerEntity self = (PlayerEntity) (Object) this;
		final float scale = ScaleUtils.getFlightScale(self);
		
		if (scale != 1.0F)
		{
			ReflectionUtils.setFlyingSpeed(self, ReflectionUtils.getFlyingSpeed(self) * scale);
		}
	}
}
