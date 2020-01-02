package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.world.World;
import virtuoel.pehkui.api.ScaleData;

@Mixin(targets = "net.minecraft.entity.mob.BlazeEntity$ShootFireballGoal")
public abstract class ShootFireballGoalMixin
{
	@Shadow @Final BlazeEntity blaze;
	
	@Redirect(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
	public boolean onSpawnEntityProxy(World obj, Entity entity_1)
	{
		final float scale = ScaleData.of(blaze).getScale();
		if(scale != 1.0F)
		{
			entity_1.setPosition(entity_1.getX(), entity_1.getY() - ((1.0D - scale) * 0.5D), entity_1.getZ());
		}
		return obj.spawnEntity(entity_1);
	}
}
