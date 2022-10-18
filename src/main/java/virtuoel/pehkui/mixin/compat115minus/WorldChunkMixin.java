package virtuoel.pehkui.mixin.compat115minus;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(WorldChunk.class)
public class WorldChunkMixin
{
	@Redirect(method = MixinConstants.GET_ENTITIES, at = @At(value = "INVOKE", target = MixinConstants.GET_BOUNDING_BOX, ordinal = 0, remap = false), remap = false)
	private Box pehkui$getEntities$getBoundingBox(Entity obj)
	{
		final Box bounds = obj.getBoundingBox();
		
		final float interactionWidth = ScaleUtils.getInteractionWidthScale(obj);
		final float interactionHeight = ScaleUtils.getInteractionHeightScale(obj);
		
		if (interactionWidth != 1.0F || interactionHeight != 1.0F)
		{
			final double scaledWidth = (obj.getWidth() * interactionWidth * 0.3D) - (obj.getWidth() * 0.3D);
			final double scaledHeight = (obj.getHeight() * interactionHeight * 0.3D) - (obj.getHeight() * 0.3D);
			
			return bounds.expand(scaledWidth, scaledHeight, scaledWidth);
		}
		
		return bounds;
	}
	
	@Redirect(method = MixinConstants.GET_ENTITIES_ENTITY_TYPE, at = @At(value = "INVOKE", target = MixinConstants.GET_BOUNDING_BOX, remap = false), remap = false)
	private Box pehkui$getEntities$getBoundingBox$type(Entity obj)
	{
		final Box bounds = obj.getBoundingBox();
		
		final float interactionWidth = ScaleUtils.getInteractionWidthScale(obj);
		final float interactionHeight = ScaleUtils.getInteractionHeightScale(obj);
		
		if (interactionWidth != 1.0F || interactionHeight != 1.0F)
		{
			final double scaledWidth = (obj.getWidth() * interactionWidth * 0.3D) - (obj.getWidth() * 0.3D);
			final double scaledHeight = (obj.getHeight() * interactionHeight * 0.3D) - (obj.getHeight() * 0.3D);
			
			return bounds.expand(scaledWidth, scaledHeight, scaledWidth);
		}
		
		return bounds;
	}
	
	@Redirect(method = MixinConstants.GET_ENTITIES_CLASS, at = @At(value = "INVOKE", target = MixinConstants.GET_BOUNDING_BOX, remap = false), remap = false)
	private Box pehkui$getEntities$getBoundingBox$class(Entity obj)
	{
		final Box bounds = obj.getBoundingBox();
		
		final float interactionWidth = ScaleUtils.getInteractionWidthScale(obj);
		final float interactionHeight = ScaleUtils.getInteractionHeightScale(obj);
		
		if (interactionWidth != 1.0F || interactionHeight != 1.0F)
		{
			final double scaledWidth = (obj.getWidth() * interactionWidth * 0.3D) - (obj.getWidth() * 0.3D);
			final double scaledHeight = (obj.getHeight() * interactionHeight * 0.3D) - (obj.getHeight() * 0.3D);
			
			return bounds.expand(scaledWidth, scaledHeight, scaledWidth);
		}
		
		return bounds;
	}
}
