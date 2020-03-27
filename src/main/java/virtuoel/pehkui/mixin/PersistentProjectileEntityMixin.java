package virtuoel.pehkui.mixin;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.world.World;
import virtuoel.pehkui.api.ScaleData;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin extends EntityMixin
{
	@Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;)V")
	private void onConstruct(EntityType<? extends ProjectileEntity> type, LivingEntity owner, World world, CallbackInfo info)
	{
		final float scale = ScaleData.of(owner).getScale();
		
		if (scale != 1.0F)
		{
			final ProjectileEntity self = ((ProjectileEntity) (Object) this);
			updatePosition(self.getX(), self.getY() + ((1.0F - scale) * 0.1D), self.getZ());
		}
	}
	
	@Inject(at = @At("HEAD"), method = "setOwner")
	private void onSetOwner(@Nullable Entity entity, CallbackInfo info)
	{
		if (entity != null)
		{
			final float scale = ScaleData.of(entity).getScale();
			
			if (scale != 1.0F)
			{
				pehkui_scaleData.setScale(scale);
				pehkui_scaleData.setTargetScale(scale);
				pehkui_scaleData.markForSync();
			}
		}
	}
}
