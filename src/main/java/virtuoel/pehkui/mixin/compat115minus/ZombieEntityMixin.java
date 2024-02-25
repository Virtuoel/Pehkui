package virtuoel.pehkui.mixin.compat115minus;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.passive.VillagerEntity;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ZombieEntity.class)
public class ZombieEntityMixin
{
	@Dynamic
	@Inject(method = MixinConstants.CONVERT_TO, at = @At(value = "INVOKE", shift = Shift.BEFORE, target = MixinConstants.ZOMBIE_COPY_POS_AND_ROT))
	private void pehkui$convertTo(EntityType<? extends ZombieEntity> entityType, CallbackInfo info, @Local ZombieEntity zombieEntity)
	{
		ScaleUtils.loadScale(zombieEntity, (Entity) (Object) this);
	}
	
	@Dynamic
	@Inject(method = MixinConstants.ON_KILLED_OTHER, at = @At(value = "INVOKE", shift = Shift.BEFORE, target = MixinConstants.ZOMBIE_VILLAGER_COPY_POS_AND_ROT))
	private void pehkui$onKilledOther(LivingEntity other, CallbackInfo info, @Local VillagerEntity villagerEntity, @Local ZombieVillagerEntity zombieVillagerEntity)
	{
		ScaleUtils.loadScale(zombieVillagerEntity, villagerEntity);
	}
}
