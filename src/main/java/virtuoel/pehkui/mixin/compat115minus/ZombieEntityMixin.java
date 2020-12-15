package virtuoel.pehkui.mixin.compat115minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.passive.VillagerEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ZombieEntity.class)
public class ZombieEntityMixin
{
	@Inject(method = "method_7200(Lnet/minecraft/class_1299;)V", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", shift = Shift.BEFORE, target = "Lnet/minecraft/class_1642;method_5719(Lnet/minecraft/class_1297;)V", remap = false), remap = false)
	private void onConvertTo(EntityType<? extends ZombieEntity> entityType, CallbackInfo info, ZombieEntity zombieEntity)
	{
		ScaleUtils.loadScale(zombieEntity, this);
	}
	
	@Inject(method = "method_5874(Lnet/minecraft/class_1309;)V", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", shift = Shift.BEFORE, target = "Lnet/minecraft/class_1641;method_5719(Lnet/minecraft/class_1297;)V", remap = false), remap = false)
	private void onOnKilledOther(LivingEntity other, CallbackInfo info, VillagerEntity villagerEntity, ZombieVillagerEntity zombieVillagerEntity)
	{
		ScaleUtils.loadScale(zombieVillagerEntity, villagerEntity);
	}
}
