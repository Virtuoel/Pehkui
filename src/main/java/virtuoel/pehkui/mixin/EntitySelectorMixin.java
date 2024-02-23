package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.command.EntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(EntitySelector.class)
public class EntitySelectorMixin
{
	@WrapOperation(method = "method_9810", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;intersects(Lnet/minecraft/util/math/Box;)Z"))
	private static boolean pehkui$method_9810$intersects(Box bounds, Operation<Boolean> original, @Local(argsOnly = true) Entity obj)
	{
		final float interactionWidth = ScaleUtils.getInteractionBoxWidthScale(obj);
		final float interactionHeight = ScaleUtils.getInteractionBoxHeightScale(obj);
		
		if (interactionWidth != 1.0F || interactionHeight != 1.0F)
		{
			final double scaledXLength = bounds.getLengthX() * 0.5D * (interactionWidth - 1.0F);
			final double scaledYLength = bounds.getLengthY() * 0.5D * (interactionHeight - 1.0F);
			final double scaledZLength = bounds.getLengthZ() * 0.5D * (interactionWidth - 1.0F);
			
			bounds = bounds.expand(scaledXLength, scaledYLength, scaledZLength);
		}
		
		return original.call(bounds);
	}
}
