package virtuoel.pehkui.mixin.compat115minus;

import java.util.Optional;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.entity.ai.brain.task.VillagerBreedTask;
import net.minecraft.entity.passive.VillagerEntity;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(VillagerBreedTask.class)
public class VillagerBreedTaskMixin
{
	@Dynamic
	@Inject(method = MixinConstants.CREATE_CHILD, at = @At(value = "INVOKE", shift = Shift.BEFORE, target = MixinConstants.SPAWN_ENTITY))
	private void pehkui$createChild(VillagerEntity villagerEntity, VillagerEntity villagerEntity2, CallbackInfoReturnable<Optional<VillagerEntity>> info, @Local(ordinal = 2) VillagerEntity child)
	{
		ScaleUtils.loadAverageScales(child, villagerEntity, villagerEntity2);
	}
}
