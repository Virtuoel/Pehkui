package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.util.math.Vec3d;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(targets = "net.minecraft.entity.mob.GhastEntity$ShootFireballGoal")
public abstract class GhastEntityShootFireballGoalMixin
{
	@Shadow @Final GhastEntity ghast;
	
	@ModifyArg(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
	private Entity tickSpawnEntityProxy(Entity entity)
	{
		final float scale = ScaleUtils.getHeightScale(ghast);
		
		if (scale != 1.0F)
		{
			final Vec3d pos = entity.getPos();
			
			entity.setPosition(pos.x, pos.y - ((1.0D - scale) * 0.5D), pos.z);
		}
		
		return entity;
	}
}
