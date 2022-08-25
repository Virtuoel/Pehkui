package virtuoel.pehkui.mixin.compat116plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.thrown.EggEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(EggEntity.class)
public class EggEntityMixin
{
	@ModifyArg(method = "onCollision(Lnet/minecraft/util/hit/HitResult;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
	private Entity pehkui$onCollision$entity(Entity entity)
	{
		ScaleUtils.loadScale(entity, (Entity) (Object) this);
		
		return entity;
	}
}
