package virtuoel.pehkui.mixin.compat116plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.EndermiteEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.HitResult;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(EnderPearlEntity.class)
public class EnderPearlEntityMixin
{
	@Inject(method = "onCollision(Lnet/minecraft/util/hit/HitResult;)V", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", shift = Shift.BEFORE, target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
	private void onOnCollision(HitResult hitResult, CallbackInfo info, Entity owner, ServerPlayerEntity player, EndermiteEntity endermiteEntity)
	{
		ScaleUtils.loadScale(endermiteEntity, (Entity) (Object) this);
	}
}
