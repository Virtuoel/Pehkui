package virtuoel.pehkui.mixin.step_height.compat1193minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.entity.Entity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(Entity.class)
public abstract class EntityMixin
{
	@WrapOperation(method = "adjustMovementForCollisions", require = 0, expect = 0, at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;stepHeight:F"))
	private float pehkui$adjustMovementForCollisions$stepHeight(Entity obj, Operation<Float> original)
	{
		final float scale = ScaleUtils.getStepHeightScale(obj);
		
		return scale != 1.0F ? original.call(obj) * scale : original.call(obj);
	}
}
