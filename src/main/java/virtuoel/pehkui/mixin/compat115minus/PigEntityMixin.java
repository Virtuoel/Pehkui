package virtuoel.pehkui.mixin.compat115minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.PigEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(PigEntity.class)
public class PigEntityMixin
{
	@Inject(method = "method_5800(Lnet/minecraft/class_1538;)V", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", shift = Shift.BEFORE, target = "Lnet/minecraft/class_1590;method_5808(DDDFF)V", remap = false), remap = false)
	private void onOnStruckByLightning(LightningEntity lightning, CallbackInfo info, ZombifiedPiglinEntity zombifiedPiglinEntity)
	{
		ScaleUtils.loadScale(zombifiedPiglinEntity, this);
	}
}
