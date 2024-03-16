package virtuoel.pehkui.mixin.compat115minus;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ZombieVillagerEntity.class)
public class ZombieVillagerEntityMixin
{
	@Dynamic
	@Inject(method = MixinConstants.FINISH_CONVERSION, at = @At(value = "INVOKE", shift = Shift.BEFORE, target = MixinConstants.VILLAGER_COPY_POS_AND_ROT))
	private void pehkui$finishConversion(ServerWorld world, CallbackInfo info, @Local VillagerEntity villagerEntity)
	{
		ScaleUtils.loadScale(villagerEntity, (Entity) (Object) this);
	}
}
