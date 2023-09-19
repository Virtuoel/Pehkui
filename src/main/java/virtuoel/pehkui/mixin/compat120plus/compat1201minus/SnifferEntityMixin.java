package virtuoel.pehkui.mixin.compat120plus.compat1201minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.SnifferEntity;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(SnifferEntity.class)
public class SnifferEntityMixin
{
	@Inject(at = @At("RETURN"), method = MixinConstants.GET_MOUNTED_HEIGHT_OFFSET, cancellable = true, remap = false)
	private void pehkui$getMountedHeightOffset(CallbackInfoReturnable<Double> info)
	{
		final float scale = ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this);
		
		if (scale != 1.0F)
		{
			info.setReturnValue(info.getReturnValue() * scale);
		}
	}
}
