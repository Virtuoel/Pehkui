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
	@Redirect(method = "method_18075", at = @At(value = "INVOKE", target = MixinConstants.GET_BOUNDING_BOX), remap = false)
	private static Box pehkui$raycast$getBoundingBox(Entity obj)
	{
		final Box bounds = obj.getBoundingBox();
		
		final float interactionWidth = ScaleUtils.getInteractionWidthScale(obj);
		final float interactionHeight = ScaleUtils.getInteractionHeightScale(obj);
		
		if (interactionWidth != 1.0F || interactionHeight != 1.0F)
		{
			final float margin = obj.getTargetingMargin();
			
			final double scaledXLength = margin * (interactionWidth - 1.0F);
			final double scaledYLength = margin * (interactionHeight - 1.0F);
			final double scaledZLength = margin * (interactionWidth - 1.0F);
			
			return bounds.expand(scaledXLength, scaledYLength, scaledZLength);
		}
		
		return bounds;
	}
}
