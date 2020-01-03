package virtuoel.pehkui.mixin;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.world.World;
import virtuoel.pehkui.api.ScaleData;

@Mixin(ProjectileEntity.class)
public abstract class ProjectileEntityMixin extends EntityMixin
{
	@Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;)V")
	public void onConstruct(EntityType<? extends ProjectileEntity> entityType_1, LivingEntity livingEntity_1, World world_1, CallbackInfo info)
	{
		final float scale = ScaleData.of(livingEntity_1).getScale();
		
		if(scale != 1.0F)
		{
			final ProjectileEntity self = ((ProjectileEntity) (Object) this);
			setPosition(self.getX(), self.getY() + ((1.0F - scale) * 0.1D), self.getZ());
		}
	}
	
	@Inject(at = @At("HEAD"), method = "setOwner")
	protected void onSetOwner(@Nullable Entity entity_1, CallbackInfo info)
	{
		if(entity_1 != null)
		{
			final float scale = ScaleData.of(entity_1).getScale();
			if(scale != 1.0F)
			{
				pehkui_scaleData.setScale(scale);
				pehkui_scaleData.setTargetScale(scale);
			}
		}
	}
}
