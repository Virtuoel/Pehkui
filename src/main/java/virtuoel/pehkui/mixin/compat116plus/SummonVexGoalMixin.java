package virtuoel.pehkui.mixin.compat116plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.VexEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(targets = "net.minecraft.entity.mob.EvokerEntity$SummonVexGoal")
public class SummonVexGoalMixin
{
	@ModifyArg(method = "castSpell()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;spawnEntityAndPassengers(Lnet/minecraft/entity/Entity;)V"))
	private Entity castSpellSpawnEntityAndPassengersProxy(Entity entity)
	{
		if (entity instanceof VexEntity)
		{
			ScaleUtils.loadScale(entity, ((VexEntity) entity).getOwner());
		}
		
		return entity;
	}
}
