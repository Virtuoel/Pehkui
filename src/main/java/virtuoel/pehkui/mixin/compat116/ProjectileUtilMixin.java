package virtuoel.pehkui.mixin.compat116;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.math.Box;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ProjectileUtil.class)
public class ProjectileUtilMixin
{
	@Redirect(method = "getEntityCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getBoundingBox()Lnet/minecraft/util/math/Box;"))
	private static Box pehkui$getEntityCollision$getBoundingBox(Entity obj)
	{
		final Box bounds = obj.getBoundingBox();
		
		final float width = ScaleUtils.getBoundingBoxWidthScale(obj);
		final float height = ScaleUtils.getBoundingBoxHeightScale(obj);
		
		final float interactionWidth = ScaleUtils.getInteractionWidthScale(obj);
		final float interactionHeight = ScaleUtils.getInteractionHeightScale(obj);
		
		if (width != 1.0F || height != 1.0F || interactionWidth != 1.0F || interactionHeight != 1.0F)
		{
			final double scaledXLength = 0.3D * ((width * interactionWidth) - 1.0F);
			final double scaledYLength = 0.3D * ((height * interactionHeight) - 1.0F);
			final double scaledZLength = 0.3D * ((width * interactionWidth) - 1.0F);
			
			return bounds.expand(scaledXLength, scaledYLength, scaledZLength);
		}
		
		return bounds;
	}
}
