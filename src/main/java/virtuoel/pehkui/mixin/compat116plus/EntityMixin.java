package virtuoel.pehkui.mixin.compat116plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.entity.Entity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(Entity.class)
public abstract class EntityMixin
{
	@ModifyExpressionValue(method = "updateSubmergedInWaterState()V", at = @At(value = "CONSTANT", args = "doubleValue=0.1111111119389534D"))
	private double pehkui$updateSubmergedInWaterState$offset(double value)
	{
		final float scale = ScaleUtils.getEyeHeightScale((Entity) (Object) this);
		
		return scale != 1.0F ? value * scale : value;
	}
}
