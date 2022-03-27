package virtuoel.pehkui.mixin.compat115minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.EndermiteEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(EnderPearlEntity.class)
public class EnderPearlEntityMixin
{
	@Inject(target = @Desc(value = MixinConstants.THROWN_ENTTIY_ON_COLLISION, args = { HitResult.class }), locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", shift = Shift.BEFORE, desc = @Desc(owner = World.class, value = MixinConstants.SPAWN_ENTITY, args = { Entity.class }, ret = boolean.class), remap = false), remap = false)
	private void onOnCollision(HitResult hitResult, CallbackInfo info, LivingEntity owner, ServerPlayerEntity player, EndermiteEntity endermiteEntity)
	{
		ScaleUtils.loadScale(endermiteEntity, (Entity) (Object) this);
	}
}
