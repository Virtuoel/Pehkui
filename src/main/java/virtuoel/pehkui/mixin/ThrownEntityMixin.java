package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.thrown.ThrownEntity;
import net.minecraft.world.World;
import virtuoel.pehkui.api.ScaleData;

@Mixin(ThrownEntity.class)
public abstract class ThrownEntityMixin extends EntityMixin
{
	@Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;)V")
	public void onConstruct(EntityType<? extends ThrownEntity> entityType_1, LivingEntity livingEntity_1, World world_1, CallbackInfo info)
	{
		final float scale = ScaleData.of(livingEntity_1).getScale();
		
		if(scale != 1.0F)
		{
			final ThrownEntity self = ((ThrownEntity) (Object) this);
			updatePosition(self.getX(), self.getY() + ((1.0F - scale) * 0.1D), self.getZ());
			
			pehkui_scaleData.setScale(scale);
			pehkui_scaleData.setTargetScale(scale);
		}
	}
}
