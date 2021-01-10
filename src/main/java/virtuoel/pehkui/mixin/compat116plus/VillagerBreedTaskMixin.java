package virtuoel.pehkui.mixin.compat116plus;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.entity.ai.brain.task.VillagerBreedTask;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(VillagerBreedTask.class)
public class VillagerBreedTaskMixin
{
	@Inject(method = "createChild(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/VillagerEntity;Lnet/minecraft/entity/passive/VillagerEntity;)Ljava/util/Optional;", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", shift = Shift.BEFORE, target = "Lnet/minecraft/server/world/ServerWorld;spawnEntityAndPassengers(Lnet/minecraft/entity/Entity;)V"))
	private void onCreateChild(ServerWorld serverWorld, VillagerEntity villagerEntity, VillagerEntity villagerEntity2, CallbackInfoReturnable<Optional<VillagerEntity>> info, VillagerEntity child)
	{
		ScaleUtils.loadAverageScales(child, villagerEntity, villagerEntity2);
	}
}
