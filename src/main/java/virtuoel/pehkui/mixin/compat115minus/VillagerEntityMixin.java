package virtuoel.pehkui.mixin.compat115minus;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.passive.VillagerEntity;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(VillagerEntity.class)
public class VillagerEntityMixin
{
	@Dynamic
	@Inject(method = MixinConstants.ON_STRUCK_BY_LIGHTNING, at = @At(value = "INVOKE", shift = Shift.BEFORE, target = MixinConstants.WITCH_REFRESH_POS_AND_ANGLES))
	private void pehkui$onStruckByLightning(LightningEntity lightning, CallbackInfo info, @Local WitchEntity witchEntity)
	{
		ScaleUtils.loadScale(witchEntity, (Entity) (Object) this);
	}
}
