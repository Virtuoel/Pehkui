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
		final float width = ScaleUtils.getBoundingBoxWidthScale(obj);
		final float height = ScaleUtils.getBoundingBoxHeightScale(obj);

		final float interactionWidth = ScaleUtils.getInteractionWidthScale(obj);
		final float interactionHeight = ScaleUtils.getInteractionHeightScale(obj);
		
		if (width != 1.0F || height != 1.0F || interactionWidth != 1.0F || interactionHeight != 1.0F)
		{
			final double scaledWidth = (width * interactionWidth * value) - value;
			final double scaledHeight = (height * interactionHeight * value) - value;
			return obj.getBoundingBox().expand(scaledWidth, scaledHeight, scaledWidth);
		}
		return obj.getBoundingBox();
	}

	@Redirect(method = "raycast", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getBoundingBox()Lnet/minecraft/util/math/Box;"))
	private static Box pehkui$raycast$getInteractionBox(Entity obj)
	{
		final float interactionWidth = ScaleUtils.getInteractionWidthScale(obj);
		final float interactionHeight = ScaleUtils.getInteractionHeightScale(obj);

		if (interactionWidth != 1.0F || interactionHeight != 1.0F)
		{
			final double scaledWidth = ((obj.getWidth() * interactionWidth * 0.30000001192092896D) + (interactionWidth * obj.getTargetingMargin())) - (obj.getWidth() * 0.30000001192092896D - obj.getTargetingMargin());
			final double scaledHeight = ((obj.getHeight() * interactionHeight * 0.30000001192092896D) + (interactionHeight * obj.getTargetingMargin())) - (obj.getHeight() * 0.30000001192092896D - obj.getTargetingMargin());

			return obj.getBoundingBox().expand(scaledWidth, scaledHeight, scaledWidth);
		}

		return obj.getBoundingBox();
	}
}
