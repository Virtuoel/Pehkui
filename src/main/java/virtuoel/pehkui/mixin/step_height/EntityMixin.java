package virtuoel.pehkui.mixin.step_height;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.Entity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(Entity.class)
public abstract class EntityMixin
{
	@Redirect(method = "adjustMovementForCollisions", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;stepHeight:F"))
	private float adjustMovementForCollisionsStepHeightProxy(Entity obj)
	{
		final float scale = ScaleUtils.getStepHeightScale(obj);
		
		return scale != 1.0F ? obj.stepHeight * scale : obj.stepHeight;
	}
}
