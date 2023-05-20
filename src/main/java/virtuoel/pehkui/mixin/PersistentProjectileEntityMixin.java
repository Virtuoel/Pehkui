package virtuoel.pehkui.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin
{
	@Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;)V")
	private void pehkui$construct(EntityType<? extends ProjectileEntity> type, LivingEntity owner, World world, CallbackInfo info)
	{
		final float scale = ScaleUtils.getEyeHeightScale(owner);
		
		if (scale != 1.0F)
		{
			final Entity self = ((Entity) (Object) this);
			
			final Vec3d pos = self.getPos();
			
			self.setPosition(pos.x, pos.y + ((1.0F - scale) * 0.1D), pos.z);
		}
	}
	
	@Inject(at = @At("HEAD"), method = "setOwner")
	private void pehkui$setOwner(@Nullable Entity entity, CallbackInfo info)
	{
		if (entity != null)
		{
			ScaleUtils.setScaleOfProjectile((Entity) (Object) this, entity);
		}
	}
	
	@ModifyArg(method = "tick", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;setVelocity(DDD)V"))
	private double pehkui$tick$gravity(double value)
	{
		final Vec3d velocity = ((Entity) (Object) this).getVelocity();
		final float scale = ScaleUtils.getMotionScale((Entity) (Object) this);
		
		return scale != 1.0F ? velocity.y + (scale * (value - velocity.y)) : value;
	}
}
