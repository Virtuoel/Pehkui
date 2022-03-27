package virtuoel.pehkui.mixin.compat115minus;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.brain.task.VillagerBreedTask;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.world.World;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(VillagerBreedTask.class)
public class VillagerBreedTaskMixin
{
	@Inject(target = @Desc(value = MixinConstants.CREATE_CHILD, args = { VillagerEntity.class, VillagerEntity.class }, ret = Optional.class), locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", shift = Shift.BEFORE, desc = @Desc(owner = World.class, value = MixinConstants.SPAWN_ENTITY, args = { Entity.class }, ret = boolean.class), remap = false), remap = false)
	private void onCreateChild(VillagerEntity villagerEntity, VillagerEntity villagerEntity2, CallbackInfoReturnable<Optional<VillagerEntity>> info, VillagerEntity child)
	{
		ScaleUtils.loadAverageScales(child, villagerEntity, villagerEntity2);
	}
}
