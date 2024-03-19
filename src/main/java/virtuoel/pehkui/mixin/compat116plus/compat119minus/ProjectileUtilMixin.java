package virtuoel.pehkui.mixin.compat116plus.compat119minus;

import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ProjectileUtil.class)
public class ProjectileUtilMixin
{
	@Dynamic
	@WrapOperation(method = MixinConstants.GET_COLLISION, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileUtil;getEntityCollision(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;)Lnet/minecraft/util/hit/EntityHitResult;"))
	private static EntityHitResult pehkui$getCollision$expand(World world, Entity entity, Vec3d min, Vec3d max, Box box, Predicate<Entity> predicate, Operation<EntityHitResult> original)
	{
		final float width = ScaleUtils.getBoundingBoxWidthScale(entity);
		final float height = ScaleUtils.getBoundingBoxHeightScale(entity);
		
		final float interactionWidth = ScaleUtils.getInteractionBoxWidthScale(entity);
		final float interactionHeight = ScaleUtils.getInteractionBoxHeightScale(entity);
		
		if (width != 1.0F || height != 1.0F || interactionWidth != 1.0F || interactionHeight != 1.0F)
		{
			box = box.expand((width * interactionWidth) - 1.0D, (height * interactionHeight) - 1.0D, (width * interactionWidth) - 1.0D);
		}
		
		return original.call(world, entity, min, max, box, predicate);
	}
}
