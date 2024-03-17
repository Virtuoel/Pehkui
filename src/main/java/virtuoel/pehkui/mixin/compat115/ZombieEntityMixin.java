package virtuoel.pehkui.mixin.compat115;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ZombieEntity.class)
public class ZombieEntityMixin
{
	@Dynamic
	@Inject(method = MixinConstants.INTERACT_MOB, at = @At(value = "INVOKE", shift = Shift.BEFORE, target = MixinConstants.ZOMBIE_REFRESH_POS_AND_ANGLES))
	private void pehkui$interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<Boolean> info, @Local ZombieEntity zombieEntity)
	{
		ScaleUtils.loadScale(zombieEntity, (Entity) (Object) this);
	}
}
