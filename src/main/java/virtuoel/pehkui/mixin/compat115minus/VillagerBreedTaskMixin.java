package virtuoel.pehkui.mixin.compat115minus;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.entity.ai.brain.task.VillagerBreedTask;
import net.minecraft.entity.passive.VillagerEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(VillagerBreedTask.class)
public class VillagerBreedTaskMixin
{
	@Inject(method = "method_18970(Lnet/minecraft/class_1646;Lnet/minecraft/class_1646;)Ljava/util/Optional;", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", shift = Shift.BEFORE, target = "Lnet/minecraft/class_1937;method_8649(Lnet/minecraft/class_1297;)Z", remap = false), remap = false)
	private void onCreateChild(VillagerEntity villagerEntity, VillagerEntity villagerEntity2, CallbackInfoReturnable<Optional<VillagerEntity>> info, VillagerEntity child)
	{
		ScaleUtils.loadAverageScales(true, child, villagerEntity, villagerEntity2);
	}
}
