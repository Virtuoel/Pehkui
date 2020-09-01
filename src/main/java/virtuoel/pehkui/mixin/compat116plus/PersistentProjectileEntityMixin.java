package virtuoel.pehkui.mixin.compat116plus;

import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin
{
	@ModifyArg(method = "getEntityCollision", index = 4, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileUtil;getEntityCollision(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;)Lnet/minecraft/util/hit/EntityHitResult;"))
	private Box getEntityCollisionModifyExpand(World world, Entity entity, Vec3d vec3d, Vec3d vec3d2, Box box, Predicate<Entity> predicate)
	{
		final float width = ScaleUtils.getWidthScale(entity);
		final float height = ScaleUtils.getHeightScale(entity);
		
		if (width != 1.0F || height != 1.0F)
		{
			return box.expand(width - 1.0D, height - 1.0D, width - 1.0D);
		}
		
		return box;
	}
}
