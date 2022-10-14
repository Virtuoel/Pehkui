package virtuoel.pehkui.mixin.client.compat116minus;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(targets = MixinConstants.PROJECTILE_UTIL, remap = false)
public class ProjectileUtilMixin
{
	@Redirect(method = "method_18075", at = @At(value = "INVOKE", target = MixinConstants.GET_BOUNDING_BOX))
	private static Box pehkui$raycast$getInteractionBox(Entity obj)
	{
		final float interactionWidth = ScaleUtils.getInteractionWidthScale(obj);
		final float interactionHeight = ScaleUtils.getInteractionHeightScale(obj);

		if (interactionWidth != 1.0F || interactionHeight != 1.0F)
		{
			final double scaledWidth = (obj.getWidth() * interactionWidth * 0.30000001192092896D) - (obj.getWidth() * 0.30000001192092896D) - obj.getTargetingMargin();
			final double scaledHeight = (obj.getHeight() * interactionHeight * 0.30000001192092896D) - (obj.getHeight() * 0.30000001192092896D) - obj.getTargetingMargin();

			return obj.getBoundingBox().expand(scaledWidth, scaledHeight, scaledWidth);
		}

		return obj.getBoundingBox();
	}
}
