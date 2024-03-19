package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.util.math.Direction;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ShulkerEntity.class)
public class ShulkerEntityMixin
{
	@ModifyReturnValue(method = "getActiveEyeHeight", at = @At("RETURN"))
	private float pehkui$getActiveEyeHeight(float original, EntityPose pose, EntityDimensions dimensions)
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
					return ScaleUtils.divideClamped(1.0F, scale) - original;
				}
				else
				{
					return ScaleUtils.divideClamped(1.0F - original, scale);
				}
			}
		}
		
		return original;
	}
}
