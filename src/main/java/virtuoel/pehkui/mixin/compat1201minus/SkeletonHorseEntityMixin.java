package virtuoel.pehkui.mixin.compat1201minus;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.SkeletonHorseEntity;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(SkeletonHorseEntity.class)
public abstract class SkeletonHorseEntityMixin
{
	@Dynamic
	@ModifyReturnValue(method = MixinConstants.GET_MOUNTED_HEIGHT_OFFSET, at = @At("RETURN"))
	private double pehkui$getMountedHeightOffset(double original)
	{
		final float scale = ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this);
		
		return scale != 1.0F ? (original + ((1.0F - scale) * 0.1875D)) : original;
	}
}
