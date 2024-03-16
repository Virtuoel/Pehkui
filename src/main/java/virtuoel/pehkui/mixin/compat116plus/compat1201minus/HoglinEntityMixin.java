package virtuoel.pehkui.mixin.compat116plus.compat1201minus;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.entity.mob.HoglinEntity;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(HoglinEntity.class)
public class HoglinEntityMixin
{
	@Dynamic
	@ModifyReturnValue(method = MixinConstants.GET_MOUNTED_HEIGHT_OFFSET, at = @At("RETURN"))
	private double pehkui$getMountedHeightOffset(double original)
	{
		final HoglinEntity self = (HoglinEntity) (Object) this;
		
		final float scale = ScaleUtils.getBoundingBoxHeightScale(self);
		
		return scale != 1.0F ? (original + ((1.0F - scale) * (self.isBaby() ? 0.2D : 0.15D))) : original;
	}
}
