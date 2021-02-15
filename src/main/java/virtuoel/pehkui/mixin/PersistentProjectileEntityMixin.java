package virtuoel.pehkui.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
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
public abstract class PersistentProjectileEntityMixin extends EntityMixin
{
	@Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;)V")
	private void onConstruct(EntityType<? extends ProjectileEntity> type, LivingEntity owner, World world, CallbackInfo info)
	{
		final float scale = ScaleUtils.getHeightScale(owner);
		
		if (scale != 1.0F)
		{
			final Entity self = ((Entity) (Object) this);
			
			final Vec3d pos = self.getPos();
			
			self.setPosition(pos.x, pos.y + ((1.0F - scale) * 0.1D), pos.z);
		}
	}
	
	@Inject(at = @At("HEAD"), method = "setOwner")
	private void onSetOwner(@Nullable Entity entity, CallbackInfo info)
	{
		if (entity != null)
		{
			ScaleUtils.setScaleOfProjectile((Entity) (Object) this, entity);
		}
	}
	
	@ModifyConstant(method = "tick", constant = @Constant(doubleValue = 0.05000000074505806D))
	private double tickModifyGravity(double value)
	{
		final float scale = ScaleUtils.getMotionScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
}
