package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.command.EntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(EntitySelector.class)
public class EntitySelectorMixin
{
	@Redirect(method = "method_9810", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getBoundingBox()Lnet/minecraft/util/math/Box;"))
	private static Box pehkui$method_9810$getBoundingBox(Entity obj)
	{
		final Box bounds = obj.getBoundingBox();
		
		final float width = ScaleUtils.getBoundingBoxWidthScale(obj);
		final float height = ScaleUtils.getBoundingBoxHeightScale(obj);
		
		final float interactionWidth = ScaleUtils.getInteractionWidthScale(obj);
		final float interactionHeight = ScaleUtils.getInteractionHeightScale(obj);
		
		if (interactionWidth != 1.0F || interactionHeight != 1.0F)
		{
			final double scaledWidth = (width * interactionWidth * 0.3D) - 0.3D;
			final double scaledHeight = (height * interactionHeight * 0.3D) - 0.3D;
			
			return bounds.expand(scaledWidth, scaledHeight, scaledWidth);
		}
		
		return bounds;
	}
}
