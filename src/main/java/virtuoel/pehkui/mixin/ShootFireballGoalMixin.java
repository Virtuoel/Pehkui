package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.world.World;
import virtuoel.pehkui.api.ResizableEntity;

@Mixin(targets = "net.minecraft.entity.mob.BlazeEntity$ShootFireballGoal")
public abstract class ShootFireballGoalMixin
{
	@Shadow @Final BlazeEntity blaze;
	
	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
	public boolean onSpawnEntityProxy(World obj, Entity entity_1)
	{
		final float scale = ((ResizableEntity) blaze).getScale();
		if(scale != 1.0F)
		{
			entity_1.y -= ((1.0D - scale) * 0.5D);
		}
		return obj.spawnEntity(entity_1);
	}
}
