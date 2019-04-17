package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import virtuoel.pehkui.api.ResizableEntity;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin
{
	@Shadow abstract float getActiveEyeHeight(EntityPose entityPose_1, EntitySize entitySize_1);
	
	@Redirect(method = "getEyeHeight", at = @At(value = "INVOKE", target = "net/minecraft/entity/LivingEntity.getActiveEyeHeight(Lnet/minecraft/entity/EntityPose;Lnet/minecraft/entity/EntitySize;)F"))
	public float onGetEyeHeightGetActiveEyeHeightProxy(LivingEntity obj, EntityPose entityPose_1, EntitySize entitySize_1)
	{
		final float scale = getScale();
		return getActiveEyeHeight(entityPose_1, entitySize_1.scaled(1.0F / scale)) * scale;
	}
	
	@Redirect(method = "updatePostDeath", at = @At(value = "INVOKE", target = "net/minecraft/world/World.spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
	public boolean onUpdatePostDeathSpawnEntityProxy(World obj, Entity entity_1)
	{
		final boolean ret = obj.spawnEntity(entity_1);
		
		final float scale = getScale();
		if(scale != 1.0F)
		{
			((ResizableEntity) entity_1).setScale(scale);
			((ResizableEntity) entity_1).setTargetScale(scale);
		}
		
		return ret;
	}
}
