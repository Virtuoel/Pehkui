package virtuoel.pehkui.mixin.step_height.compat1194plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.entity.Entity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(Entity.class)
public abstract class EntityMixin
{
	/* // TODO 1.19.4
	@ModifyReturnValue(method = "getStepHeight()F", at = @At("RETURN"))
	private float pehkui$getStepHeight(float original)
	{
		final float scale = ScaleUtils.getStepHeightScale((Entity) (Object) this);
		
		return scale != 1.0F ? original * scale : original;
	}
	*/
}
