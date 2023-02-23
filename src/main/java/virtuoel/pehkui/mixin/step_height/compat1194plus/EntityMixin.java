package virtuoel.pehkui.mixin.step_height.compat1194plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(Entity.class)
public abstract class EntityMixin
{
	@Inject(method = "method_49476()F", at = @At("RETURN"), cancellable = true)
	private void pehkui$getStepHeight(CallbackInfoReturnable<Float> info)
	{
		final float scale = ScaleUtils.getStepHeightScale((Entity) (Object) this);
		
		if (scale != 1.0F)
		{
			info.setReturnValue(info.getReturnValueF() * scale);
		}
	}
}
