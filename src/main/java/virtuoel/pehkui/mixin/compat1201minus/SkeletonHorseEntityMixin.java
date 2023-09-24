package virtuoel.pehkui.mixin.compat1201minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.SkeletonHorseEntity;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(SkeletonHorseEntity.class)
public abstract class SkeletonHorseEntityMixin
{
	@Inject(at = @At("RETURN"), method = MixinConstants.GET_MOUNTED_HEIGHT_OFFSET, cancellable = true)
	private void pehkui$getMountedHeightOffset(CallbackInfoReturnable<Double> info)
	{
		final float scale = ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this);
		
		if (scale != 1.0F)
		{
			info.setReturnValue(info.getReturnValue() + ((1.0F - scale) * 0.1875D));
		}
	}
}
