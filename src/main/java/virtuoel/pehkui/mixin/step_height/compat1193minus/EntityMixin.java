package virtuoel.pehkui.mixin.step_height.compat1193minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.Entity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(Entity.class)
public abstract class EntityMixin
{
	@Shadow
	float stepHeight;
	
	@Redirect(method = "adjustMovementForCollisions", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;stepHeight:F"))
	private float pehkui$adjustMovementForCollisions$stepHeight(Entity obj)
	{
		final float scale = ScaleUtils.getStepHeightScale((Entity) (Object) this);
		
		return scale != 1.0F ? stepHeight * scale : stepHeight;
	}
}
