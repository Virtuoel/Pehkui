package virtuoel.pehkui.mixin.compat115minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.passive.VillagerEntity;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(VillagerEntity.class)
public class VillagerEntityMixin
{
	@Inject(target = @Desc(value = MixinConstants.ON_STRUCK_BY_LIGHTNING, args = { LightningEntity.class }), locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", shift = Shift.BEFORE, desc = @Desc(owner = WitchEntity.class, value = MixinConstants.REFRESH_POS_AND_ANGLES, args = { double.class, double.class, double.class, float.class, float.class }), remap = false), remap = false)
	private void onOnStruckByLightning(LightningEntity lightning, CallbackInfo info, WitchEntity witchEntity)
	{
		ScaleUtils.loadScale(witchEntity, (Entity) (Object) this);
	}
}
