package virtuoel.pehkui.mixin.compat115minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.util.hit.HitResult;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(EggEntity.class)
public class EggEntityMixin
{
	@Inject(method = "method_7492(Lnet/minecraft/class_239;)V", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", shift = Shift.BEFORE, target = "Lnet/minecraft/class_1937;method_8649(Lnet/minecraft/class_1297;)Z", remap = false), remap = false)
	private void onOnCollision(HitResult hitResult, CallbackInfo info, int i, int j, ChickenEntity chickenEntity)
	{
		ScaleUtils.loadScale(chickenEntity, this, true);
	}
}
