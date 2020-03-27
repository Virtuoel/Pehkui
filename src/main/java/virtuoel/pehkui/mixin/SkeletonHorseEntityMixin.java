package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.mob.SkeletonHorseEntity;

@Mixin(SkeletonHorseEntity.class)
public abstract class SkeletonHorseEntityMixin extends EntityMixin
{
	@Inject(at = @At("RETURN"), method = "getMountedHeightOffset", cancellable = true)
	private void onGetMountedHeightOffset(CallbackInfoReturnable<Double> info)
	{
		final float scale = pehkui_scaleData.getScale();
		
		if (scale != 1.0F)
		{
			info.setReturnValue(info.getReturnValue() + ((1.0F - scale) * 0.1875D));
		}
	}
}
