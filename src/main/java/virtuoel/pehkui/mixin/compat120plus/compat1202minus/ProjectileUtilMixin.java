package virtuoel.pehkui.mixin.compat120plus.compat1202minus;

import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ProjectileUtil.class)
public class ProjectileUtilMixin
{
	@ModifyArg(method = "getCollision(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/entity/Entity;Ljava/util/function/Predicate;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/world/World;)Lnet/minecraft/util/hit/HitResult;", index = 4, at = @At(value = "INVOKE", target = MixinConstants.GET_ENTITY_COLLISION))
	private static Box pehkui$getCollision$expand(World world, Entity entity, Vec3d min, Vec3d max, Box box, Predicate<Entity> predicate)
	{
		final float width = ScaleUtils.getBoundingBoxWidthScale(entity);
		final float height = ScaleUtils.getBoundingBoxHeightScale(entity);
		
		final float interactionWidth = ScaleUtils.getInteractionBoxWidthScale(entity);
		final float interactionHeight = ScaleUtils.getInteractionBoxHeightScale(entity);
		
		if (width != 1.0F || height != 1.0F || interactionWidth != 1.0F || interactionHeight != 1.0F)
		{
			return box.expand((width * interactionWidth) - 1.0D, (height * interactionHeight) - 1.0D, (width * interactionWidth) - 1.0D);
		}
		
		return box;
	}
}
