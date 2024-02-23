package virtuoel.pehkui.mixin.compat116;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.entity.Entity;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(Entity.class)
public abstract class EntityMixin
{
	@Dynamic
	@ModifyExpressionValue(method = MixinConstants.IS_INSIDE_WALL, at = @At(value = "CONSTANT", args = "doubleValue=0.10000000149011612D"))
	private double pehkui$isInsideWall$offset(double value)
	{
		final float scale = ScaleUtils.getEyeHeightScale((Entity) (Object) this);
		
		return scale != 1.0F ? value * scale : value;
	}
}
