package virtuoel.pehkui.mixin.compat116plus.compat1201minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.mob.HoglinEntity;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(HoglinEntity.class)
public class HoglinEntityMixin
{
	@Inject(at = @At("RETURN"), method = MixinConstants.GET_MOUNTED_HEIGHT_OFFSET, cancellable = true, remap = false)
	private void pehkui$getMountedHeightOffset(CallbackInfoReturnable<Double> info)
	{
		final HoglinEntity self = (HoglinEntity) (Object) this;
		
		final float scale = ScaleUtils.getBoundingBoxHeightScale(self);
		
		if (scale != 1.0F)
		{
			info.setReturnValue(info.getReturnValue() + ((1.0F - scale) * (self.isBaby() ? 0.2D : 0.15D)));
		}
	}
}
