package virtuoel.pehkui.mixin.compat116plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.server.world.ServerWorld;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(PigEntity.class)
public class PigEntityMixin
{
	@Inject(method = "onStruckByLightning(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LightningEntity;)V", require = 0, expect = 0, at = @At(value = "INVOKE", shift = Shift.BEFORE, target = "Lnet/minecraft/entity/mob/ZombifiedPiglinEntity;refreshPositionAndAngles(DDDFF)V"))
	private void pehkui$onStruckByLightning(ServerWorld world, LightningEntity lightning, CallbackInfo info, @Local ZombifiedPiglinEntity zombifiedPiglinEntity)
	{
		ScaleUtils.loadScale(zombifiedPiglinEntity, (Entity) (Object) this);
	}
}
