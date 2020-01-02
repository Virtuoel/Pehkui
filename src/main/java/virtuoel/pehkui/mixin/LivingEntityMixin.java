package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import virtuoel.pehkui.api.ScaleData;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin
{
	@Shadow abstract float getActiveEyeHeight(EntityPose entityPose_1, EntityDimensions entitySize_1);
	
	@Redirect(method = "getEyeHeight", at = @At(value = "INVOKE", target = "net/minecraft/entity/LivingEntity.getActiveEyeHeight(Lnet/minecraft/entity/EntityPose;Lnet/minecraft/entity/EntityDimensions;)F"))
	public float onGetEyeHeightGetActiveEyeHeightProxy(LivingEntity obj, EntityPose entityPose_1, EntityDimensions entitySize_1)
	{
		final float scale = pehkui_scaleData.getScale();
		return getActiveEyeHeight(entityPose_1, entitySize_1.scaled(1.0F / scale)) * scale;
	}
	
	@Redirect(method = "method_23883", at = @At(value = "INVOKE", target = "net/minecraft/world/World.spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
	public boolean method_23883SpawnEntityProxy(World obj, Entity entity_1)
	{
		final boolean ret = obj.spawnEntity(entity_1);
		
		final float scale = pehkui_scaleData.getScale();
		if(scale != 1.0F)
		{
			final ScaleData data = ScaleData.of(entity_1);
			data.setScale(scale);
			data.setTargetScale(scale);
		}
		
		return ret;
	}
	
	@Redirect(method = "method_23733", at = @At(value = "INVOKE", target = "net/minecraft/world/World.spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
	public boolean method_23733SpawnEntityProxy(World obj, Entity entity_1)
	{
		final boolean ret = obj.spawnEntity(entity_1);
		
		final float scale = pehkui_scaleData.getScale();
		if(scale != 1.0F)
		{
			final ScaleData data = ScaleData.of(entity_1);
			data.setScale(scale);
			data.setTargetScale(scale);
		}
		
		return ret;
	}
}
