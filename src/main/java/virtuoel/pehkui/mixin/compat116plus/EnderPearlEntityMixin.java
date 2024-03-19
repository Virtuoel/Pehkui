package virtuoel.pehkui.mixin.compat116plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.EndermiteEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.util.hit.HitResult;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(EnderPearlEntity.class)
public class EnderPearlEntityMixin
{
	@Inject(method = "onCollision(Lnet/minecraft/util/hit/HitResult;)V", at = @At(value = "INVOKE", shift = Shift.BEFORE, target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
	private void pehkui$onCollision(HitResult hitResult, CallbackInfo info, @Local EndermiteEntity endermiteEntity)
	{
		ScaleUtils.loadScale(endermiteEntity, (Entity) (Object) this);
	}
}
