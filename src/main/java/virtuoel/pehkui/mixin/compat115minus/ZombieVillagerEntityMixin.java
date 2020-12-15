package virtuoel.pehkui.mixin.compat115minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ZombieVillagerEntity.class)
public class ZombieVillagerEntityMixin
{
	@Inject(method = "method_7197(Lnet/minecraft/class_3218;)V", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", shift = Shift.BEFORE, target = "Lnet/minecraft/class_1646;method_5719(Lnet/minecraft/class_1297;)V", remap = false), remap = false)
	private void onFinishConversion(ServerWorld world, CallbackInfo info, VillagerEntity villagerEntity)
	{
		ScaleUtils.loadScale(villagerEntity, this);
	}
}
