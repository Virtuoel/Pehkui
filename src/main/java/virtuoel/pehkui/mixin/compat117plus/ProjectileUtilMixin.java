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
	@Redirect(method = "method_37226", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getBoundingBox()Lnet/minecraft/util/math/Box;"))
	private static Box getEntityCollisionModifyGetBoundingBox(Entity obj, World world, Entity except, Vec3d vec3d, Vec3d vec3d2, Box box, Predicate<Entity> predicate, float value)
	{
		final float width = ScaleUtils.getBoundingBoxWidthScale(obj);
		final float height = ScaleUtils.getBoundingBoxHeightScale(obj);
		
		if (width != 1.0F || height != 1.0F)
		{
			final double scaledWidth = (width * value) - value;
			final double scaledHeight = (height * value) - value;
			return obj.getBoundingBox().expand(scaledWidth, scaledHeight, scaledWidth);
		}
		
		return obj.getBoundingBox();
	}
}
