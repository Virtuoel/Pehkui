package virtuoel.pehkui.mixin.compat115minus;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.PigEntity;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(PigEntity.class)
public class PigEntityMixin
{
	@Dynamic
	@Inject(method = MixinConstants.ON_STRUCK_BY_LIGHTNING, at = @At(value = "INVOKE", shift = Shift.BEFORE, target = MixinConstants.ZOMBIFIED_PIGLIN_REFRESH_POS_AND_ANGLES))
	private void pehkui$onStruckByLightning(LightningEntity lightning, CallbackInfo info, @Local ZombifiedPiglinEntity zombifiedPiglinEntity)
	{
		ScaleUtils.loadScale(zombifiedPiglinEntity, (Entity) (Object) this);
	}
	
	@Dynamic
	@ModifyExpressionValue(method = MixinConstants.TRAVEL, at = @At(value = "CONSTANT", args = "floatValue=4.0F"))
	private float pehkui$travel$limbDistance(float value)
	{
		return ScaleUtils.modifyLimbDistance(value, (Entity) (Object) this);
	}
}
