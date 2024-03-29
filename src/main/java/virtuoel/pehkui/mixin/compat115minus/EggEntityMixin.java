package virtuoel.pehkui.mixin.compat115minus;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.util.hit.HitResult;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(EggEntity.class)
public class EggEntityMixin
{
	@Dynamic
	@Inject(method = MixinConstants.ON_COLLISION, at = @At(value = "INVOKE", shift = Shift.BEFORE, target = MixinConstants.SPAWN_ENTITY))
	private void pehkui$onCollision(HitResult hitResult, CallbackInfo info, @Local ChickenEntity chickenEntity)
	{
		ScaleUtils.loadScale(chickenEntity, (Entity) (Object) this);
	}
}
