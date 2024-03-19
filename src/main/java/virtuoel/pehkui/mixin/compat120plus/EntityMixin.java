package virtuoel.pehkui.mixin.compat120plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.entity.Entity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(Entity.class)
public class EntityMixin
{
	@ModifyExpressionValue(method = "updateSupportingBlockPos", at = @At(value = "CONSTANT", args = "doubleValue=1.0E-6"))
	private double pehkui$updateSupportingBlockPos$offset(double value)
	{
		final float scale = ScaleUtils.getMotionScale((Entity) (Object) this);
		
		return scale < 1.0F ? value * scale : value;
	}
}
