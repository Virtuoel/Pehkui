package virtuoel.pehkui.mixin.compat115minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.VexEntity;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(targets = "net.minecraft.entity.mob.EvokerEntity$SummonVexGoal")
public class SummonVexGoalMixin
{
	@ModifyArg(method = MixinConstants.CAST_SPELL, at = @At(value = "INVOKE", target = MixinConstants.SPAWN_ENTITY, remap = false), remap = false)
	private Entity pehkui$castSpell$spawnEntity(Entity entity)
	{
		if (entity instanceof VexEntity)
		{
			ScaleUtils.loadScale(entity, ((VexEntity) entity).getOwner());
		}
		
		return entity;
	}
}
