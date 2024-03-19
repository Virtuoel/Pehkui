package virtuoel.pehkui.mixin.compat115minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.entity.Entity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(Entity.class)
public abstract class EntityMixin
{
	@ModifyExpressionValue(method = "isInsideWall()Z", at = @At(value = "CONSTANT", args = "floatValue=0.1F"))
	private float pehkui$isInsideWall$offset(float value)
	{
		final float scale = ScaleUtils.getEyeHeightScale((Entity) (Object) this);
		
		return scale != 1.0F ? value * scale : value;
	}
}
