package virtuoel.pehkui.mixin.compat118plus.compat1192minus;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.world.entity.EntityLike;
import net.minecraft.world.entity.EntityTrackingSection;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(EntityTrackingSection.class)
public class EntityTrackingSectionMixin
{
	@Dynamic
	@WrapOperation(method = MixinConstants.FOR_EACH, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/EntityLike;getBoundingBox()Lnet/minecraft/util/math/Box;"))
	private Box pehkui$forEach$getBoundingBox(EntityLike obj, Operation<Box> original)
	{
		final Box bounds = original.call(obj);
		
		if (obj instanceof Entity)
		{
			final Entity entity = (Entity) obj;
			
			final float interactionWidth = ScaleUtils.getInteractionBoxWidthScale(entity);
			final float interactionHeight = ScaleUtils.getInteractionBoxHeightScale(entity);
			
			if (interactionWidth != 1.0F || interactionHeight != 1.0F)
			{
				final double scaledXLength = bounds.getLengthX() * 0.5D * (interactionWidth - 1.0F);
				final double scaledYLength = bounds.getLengthY() * 0.5D * (interactionHeight - 1.0F);
				final double scaledZLength = bounds.getLengthZ() * 0.5D * (interactionWidth - 1.0F);
				
				return bounds.expand(scaledXLength, scaledYLength, scaledZLength);
			}
		}
		
		return bounds;
	}
	
	@Dynamic
	@WrapOperation(method = MixinConstants.FOR_EACH_FILTERED, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/EntityLike;getBoundingBox()Lnet/minecraft/util/math/Box;"))
	private Box pehkui$forEach$getBoundingBox$filtered(EntityLike obj, Operation<Box> original)
	{
		final Box bounds = original.call(obj);
		
		if (obj instanceof Entity)
		{
			final Entity entity = (Entity) obj;
			
			final float interactionWidth = ScaleUtils.getInteractionBoxWidthScale(entity);
			final float interactionHeight = ScaleUtils.getInteractionBoxHeightScale(entity);
			
			if (interactionWidth != 1.0F || interactionHeight != 1.0F)
			{
				final double scaledXLength = bounds.getLengthX() * 0.5D * (interactionWidth - 1.0F);
				final double scaledYLength = bounds.getLengthY() * 0.5D * (interactionHeight - 1.0F);
				final double scaledZLength = bounds.getLengthZ() * 0.5D * (interactionWidth - 1.0F);
				
				return bounds.expand(scaledXLength, scaledYLength, scaledZLength);
			}
		}
		
		return bounds;
	}
}
