package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.util.math.Direction;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ShulkerEntity.class)
public class ShulkerEntityMixin
{
	@Inject(method = "getActiveEyeHeight", at = @At("RETURN"), cancellable = true)
	private void onGetActiveEyeHeight(EntityPose pose, EntityDimensions dimensions, CallbackInfoReturnable<Float> info)
	{
		final ShulkerEntity entity = (ShulkerEntity) (Object) this;
		
		final Direction face = entity.getAttachedFace();
		if (face != Direction.DOWN)
		{
			final float scale = ScaleUtils.getEyeHeightScale(entity);
			if (scale != 1.0F)
			{
				if (face == Direction.UP)
				{
					info.setReturnValue(ScaleUtils.divideClamped(1.0F, scale, ScaleUtils.DEFAULT_MINIMUM_POSITIVE_SCALE) - info.getReturnValueF());
				}
				else
				{
					info.setReturnValue(ScaleUtils.divideClamped(1.0F - info.getReturnValueF(), scale, ScaleUtils.DEFAULT_MINIMUM_POSITIVE_SCALE));
				}
			}
		}
	}
}
