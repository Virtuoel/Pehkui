package virtuoel.pehkui.mixin.compat115minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.world.chunk.WorldChunk;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(WorldChunk.class)
public class WorldChunkMixin
{
	@Redirect(method = MixinConstants.GET_ENTITIES, at = @At(value = "INVOKE", target = MixinConstants.GET_BOUNDING_BOX, ordinal = 0, remap = false), remap = false)
	private Box pehkui$getEntities$getBoundingBox(Entity obj)
	{
		final Box bounds = obj.getBoundingBox();
		
		final float interactionWidth = ScaleUtils.getInteractionBoxWidthScale(obj);
		final float interactionHeight = ScaleUtils.getInteractionBoxHeightScale(obj);
		
		if (interactionWidth != 1.0F || interactionHeight != 1.0F)
		{
			final double scaledXLength = bounds.getXLength() * 0.5D * (interactionWidth - 1.0F);
			final double scaledYLength = bounds.getYLength() * 0.5D * (interactionHeight - 1.0F);
			final double scaledZLength = bounds.getZLength() * 0.5D * (interactionWidth - 1.0F);
			
			return bounds.expand(scaledXLength, scaledYLength, scaledZLength);
		}
		
		return bounds;
	}
	
	@Redirect(method = MixinConstants.GET_ENTITIES_ENTITY_TYPE, at = @At(value = "INVOKE", target = MixinConstants.GET_BOUNDING_BOX, remap = false), remap = false)
	private Box pehkui$getEntities$getBoundingBox$type(Entity obj)
	{
		final Box bounds = obj.getBoundingBox();
		
		final float interactionWidth = ScaleUtils.getInteractionBoxWidthScale(obj);
		final float interactionHeight = ScaleUtils.getInteractionBoxHeightScale(obj);
		
		if (interactionWidth != 1.0F || interactionHeight != 1.0F)
		{
			final double scaledXLength = bounds.getXLength() * 0.5D * (interactionWidth - 1.0F);
			final double scaledYLength = bounds.getYLength() * 0.5D * (interactionHeight - 1.0F);
			final double scaledZLength = bounds.getZLength() * 0.5D * (interactionWidth - 1.0F);
			
			return bounds.expand(scaledXLength, scaledYLength, scaledZLength);
		}
		
		return bounds;
	}
	
	@Redirect(method = MixinConstants.GET_ENTITIES_CLASS, at = @At(value = "INVOKE", target = MixinConstants.GET_BOUNDING_BOX, remap = false), remap = false)
	private Box pehkui$getEntities$getBoundingBox$class(Entity obj)
	{
		final Box bounds = obj.getBoundingBox();
		
		final float interactionWidth = ScaleUtils.getInteractionBoxWidthScale(obj);
		final float interactionHeight = ScaleUtils.getInteractionBoxHeightScale(obj);
		
		if (interactionWidth != 1.0F || interactionHeight != 1.0F)
		{
			final double scaledXLength = bounds.getXLength() * 0.5D * (interactionWidth - 1.0F);
			final double scaledYLength = bounds.getYLength() * 0.5D * (interactionHeight - 1.0F);
			final double scaledZLength = bounds.getZLength() * 0.5D * (interactionWidth - 1.0F);
			
			return bounds.expand(scaledXLength, scaledYLength, scaledZLength);
		}
		
		return bounds;
	}
}
