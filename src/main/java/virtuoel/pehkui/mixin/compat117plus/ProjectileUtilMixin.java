package virtuoel.pehkui.mixin.compat117plus;

import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ProjectileUtil.class)
public class ProjectileUtilMixin
{
	@Redirect(method = "getEntityCollision(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;F)Lnet/minecraft/util/hit/EntityHitResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getBoundingBox()Lnet/minecraft/util/math/Box;"))
	private static Box pehkui$getEntityCollision$getBoundingBox(Entity obj, World world, Entity except, Vec3d vec3d, Vec3d vec3d2, Box box, Predicate<Entity> predicate, float value)
	{
		final Box bounds = obj.getBoundingBox();
		
		final float width = ScaleUtils.getBoundingBoxWidthScale(obj);
		final float height = ScaleUtils.getBoundingBoxHeightScale(obj);
		
		final float interactionWidth = ScaleUtils.getInteractionWidthScale(obj);
		final float interactionHeight = ScaleUtils.getInteractionHeightScale(obj);
		
		if (width != 1.0F || height != 1.0F || interactionWidth != 1.0F || interactionHeight != 1.0F)
		{
			final double scaledXLength = value * ((width * interactionWidth) - 1.0F);
			final double scaledYLength = value * ((height * interactionHeight) - 1.0F);
			final double scaledZLength = value * ((width * interactionWidth) - 1.0F);
			
			return bounds.expand(scaledXLength, scaledYLength, scaledZLength);
		}
		
		return bounds;
	}
	
	@Redirect(method = "raycast", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getBoundingBox()Lnet/minecraft/util/math/Box;"))
	private static Box pehkui$raycast$getInteractionBox(Entity obj)
	{
		final Box bounds = obj.getBoundingBox();
		final float margin = obj.getTargetingMargin();
		
		final float interactionWidth = ScaleUtils.getInteractionWidthScale(obj);
		final float interactionHeight = ScaleUtils.getInteractionHeightScale(obj);
		
		if (interactionWidth != 1.0F || interactionHeight != 1.0F)
		{
			final double scaledXLength = bounds.getXLength() * 0.5D * (interactionWidth - 1.0F);
			final double scaledYLength = bounds.getYLength() * 0.5D * (interactionHeight - 1.0F);
			final double scaledZLength = bounds.getZLength() * 0.5D * (interactionWidth - 1.0F);
			final double scaledMarginWidth = margin * (interactionWidth - 1.0F);
			final double scaledMarginHeight = margin * (interactionHeight - 1.0F);
			
			return bounds.expand(scaledXLength + scaledMarginWidth, scaledYLength + scaledMarginHeight, scaledZLength + scaledMarginWidth);
		}
		
		return bounds;
	}
}
