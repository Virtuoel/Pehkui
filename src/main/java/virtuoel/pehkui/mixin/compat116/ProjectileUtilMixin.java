package virtuoel.pehkui.mixin.compat116;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.math.Box;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ProjectileUtil.class)
public class ProjectileUtilMixin
{
	@WrapOperation(method = "getEntityCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getBoundingBox()Lnet/minecraft/util/math/Box;"))
	private static Box pehkui$getEntityCollision$getBoundingBox(Entity obj, Operation<Box> original)
	{
		final Box bounds = original.call(obj);
		
		final float width = ScaleUtils.getBoundingBoxWidthScale(obj);
		final float height = ScaleUtils.getBoundingBoxHeightScale(obj);
		
		final float interactionWidth = ScaleUtils.getInteractionBoxWidthScale(obj);
		final float interactionHeight = ScaleUtils.getInteractionBoxHeightScale(obj);
		
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
