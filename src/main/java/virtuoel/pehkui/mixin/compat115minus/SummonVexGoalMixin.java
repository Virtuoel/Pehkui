package virtuoel.pehkui.mixin.compat115minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.world.World;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(targets = "net.minecraft.entity.mob.EvokerEntity$SummonVexGoal")
public class SummonVexGoalMixin
{
	@ModifyArg(target = @Desc(value = MixinConstants.CAST_SPELL), at = @At(value = "INVOKE", desc = @Desc(owner = World.class, value = MixinConstants.SPAWN_ENTITY, args = { Entity.class }, ret = boolean.class), remap = false), remap = false)
	private Entity castSpellSpawnEntityProxy(Entity entity)
	{
		if (entity instanceof VexEntity)
		{
			ScaleUtils.loadScale(entity, ((VexEntity) entity).getOwner());
		}
		
		return entity;
	}
}
