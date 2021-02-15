package virtuoel.pehkui.mixin.compat116plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.server.world.ServerWorld;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(PigEntity.class)
public class PigEntityMixin
{
	@Inject(method = "onStruckByLightning(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LightningEntity;)V", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", shift = Shift.BEFORE, target = "Lnet/minecraft/entity/mob/ZombifiedPiglinEntity;refreshPositionAndAngles(DDDFF)V"))
	private void onOnStruckByLightning(ServerWorld world, LightningEntity lightning, CallbackInfo info, ZombifiedPiglinEntity zombifiedPiglinEntity)
	{
		ScaleUtils.loadScale(zombifiedPiglinEntity, (Entity) (Object) this);
	}
}
