package virtuoel.pehkui.mixin.compat115minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

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
	@Inject(target = @Desc(value = MixinConstants.CONVERT_TO, args = { EntityType.class }), locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", shift = Shift.BEFORE, desc = @Desc(owner = ZombieEntity.class, value = MixinConstants.COPY_POS_AND_ROT, args = { Entity.class }), remap = false), remap = false)
	private void onConvertTo(EntityType<? extends ZombieEntity> entityType, CallbackInfo info, ZombieEntity zombieEntity)
	{
		ScaleUtils.loadScale(zombieEntity, (Entity) (Object) this);
	}
	
	@Inject(target = @Desc(value = MixinConstants.ON_KILLED_OTHER, args = { LivingEntity.class }), locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", shift = Shift.BEFORE, desc = @Desc(owner = ZombieVillagerEntity.class, value = MixinConstants.COPY_POS_AND_ROT, args = { Entity.class }), remap = false), remap = false)
	private void onOnKilledOther(LivingEntity other, CallbackInfo info, VillagerEntity villagerEntity, ZombieVillagerEntity zombieVillagerEntity)
	{
		ScaleUtils.loadScale(zombieVillagerEntity, villagerEntity);
	}
}
