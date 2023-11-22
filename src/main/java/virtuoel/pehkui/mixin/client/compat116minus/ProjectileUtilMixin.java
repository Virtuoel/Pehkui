package virtuoel.pehkui.mixin.client.compat116minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.math.Box;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ProjectileUtil.class)
public class ProjectileUtilMixin
{
	@Redirect(method = MixinConstants.PROJECTILE_RAYCAST, require = 0, expect = 0, at = @At(value = "INVOKE", target = MixinConstants.GET_BOUNDING_BOX))
	private static Box pehkui$raycast$getBoundingBox(Entity obj)
	{
		final Box bounds = obj.getBoundingBox();
		final float margin = obj.getTargetingMargin();
		
		final float interactionWidth = ScaleUtils.getInteractionBoxWidthScale(obj);
		final float interactionHeight = ScaleUtils.getInteractionBoxHeightScale(obj);
		
		if (interactionWidth != 1.0F || interactionHeight != 1.0F)
		{
			final double scaledXLength = bounds.getLengthX() * 0.5D * (interactionWidth - 1.0F);
			final double scaledYLength = bounds.getLengthY() * 0.5D * (interactionHeight - 1.0F);
			final double scaledZLength = bounds.getLengthZ() * 0.5D * (interactionWidth - 1.0F);
			final double scaledMarginWidth = margin * (interactionWidth - 1.0F);
			final double scaledMarginHeight = margin * (interactionHeight - 1.0F);
			
			return bounds.expand(scaledXLength + scaledMarginWidth, scaledYLength + scaledMarginHeight, scaledZLength + scaledMarginWidth);
		}
		
		return bounds;
	}
}
