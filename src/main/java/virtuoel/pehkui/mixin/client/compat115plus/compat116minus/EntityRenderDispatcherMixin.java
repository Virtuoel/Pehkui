package virtuoel.pehkui.mixin.client.compat115plus.compat116minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import virtuoel.pehkui.util.ScaleRenderUtils;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin
{
	@Inject(method = "renderHitbox", at = @At(value = "TAIL"))
	private static void pehkui$renderHitbox(MatrixStack matrices, VertexConsumer vertices, Entity entity, float tickDelta, CallbackInfo ci)
	{
		final float interactionWidth = ScaleUtils.getInteractionWidthScale(entity);
		final float interactionHeight = ScaleUtils.getInteractionHeightScale(entity);
		
		if (interactionWidth != 1.0F || interactionHeight != 1.0F)
		{
			Box bounds = entity.getBoundingBox();
			
			final double scaledXLength = bounds.getXLength() * 0.5D * (interactionWidth - 1.0F);
			final double scaledYLength = bounds.getYLength() * 0.5D * (interactionHeight - 1.0F);
			final double scaledZLength = bounds.getZLength() * 0.5D * (interactionWidth - 1.0F);
			
			bounds = bounds.expand(scaledXLength, scaledYLength, scaledZLength).offset(-entity.getX(), -entity.getY(), -entity.getZ());
			
			ScaleRenderUtils.renderInteractionBox(matrices, vertices, bounds);
		}
	}
}
