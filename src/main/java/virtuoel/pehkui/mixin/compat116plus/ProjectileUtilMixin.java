package virtuoel.pehkui.mixin.compat116plus;

import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
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
	@ModifyArg(method = "getCollision", index = 4, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileUtil;getEntityCollision(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;)Lnet/minecraft/util/hit/EntityHitResult;"))
	private static Box getEntityCollisionModifyExpand(World world, Entity entity, Vec3d vec3d, Vec3d vec3d2, Box box, Predicate<Entity> predicate)
	{
		final float width = ScaleUtils.getWidthScale(entity);
		final float height = ScaleUtils.getHeightScale(entity);
		
		if (width != 1.0F || height != 1.0F)
		{
			return box.expand(width - 1.0D, height - 1.0D, width - 1.0D);
		}
		
		return box;
	}
	
	@Redirect(method = "getEntityCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getBoundingBox()Lnet/minecraft/util/math/Box;"))
	private static Box getEntityCollisionModifyGetBoundingBox(Entity obj)
	{
		final float width = ScaleUtils.getWidthScale(obj);
		final float height = ScaleUtils.getHeightScale(obj);
		
		if (width != 1.0F || height != 1.0F)
		{
			final double scaledWidth = (width * 0.30000001192092896D) - 0.30000001192092896D;
			final double scaledHeight = (height * 0.30000001192092896D) - 0.30000001192092896D;
			return obj.getBoundingBox().expand(scaledWidth, scaledHeight, scaledWidth);
		}
		
		return obj.getBoundingBox();
	}
}
