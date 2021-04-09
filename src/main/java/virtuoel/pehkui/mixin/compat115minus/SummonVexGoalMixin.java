package virtuoel.pehkui.mixin.compat115minus;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.EvokerEntity;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(targets = "net.minecraft.entity.mob.EvokerEntity$SummonVexGoal")
public class SummonVexGoalMixin
{
	@Shadow @Final EvokerEntity field_7267; // UNMAPPED_FIELD
	
	@ModifyArg(method = MixinConstants.CAST_SPELL, at = @At(value = "INVOKE", target = MixinConstants.SPAWN_ENTITY, remap = false), remap = false)
	private Entity castSpellSpawnEntityProxy(Entity vexEntity)
	{
		ScaleUtils.loadScale(vexEntity, field_7267);
		
		return vexEntity;
	}
}
