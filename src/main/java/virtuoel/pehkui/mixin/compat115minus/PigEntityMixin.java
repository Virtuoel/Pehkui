package virtuoel.pehkui.mixin.compat115minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.PigEntity;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(PigEntity.class)
public class PigEntityMixin
{
	@Inject(method = MixinConstants.ON_STRUCK_BY_LIGHTNING, locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", shift = Shift.BEFORE, target = MixinConstants.ZOMBIFIED_PIGLIN_REFRESH_POS_AND_ANGLES, remap = false), remap = false)
	private void onOnStruckByLightning(LightningEntity lightning, CallbackInfo info, ZombifiedPiglinEntity zombifiedPiglinEntity)
	{
		ScaleUtils.loadScale(zombifiedPiglinEntity, (Entity) (Object) this);
	}
	
	@ModifyConstant(method = "travel", constant = @Constant(floatValue = 4.0F))
	private float modifyLimbDistance(float value)
	{
		return ScaleUtils.modifyLimbDistance(value, (Entity) (Object) this);
	}
}
