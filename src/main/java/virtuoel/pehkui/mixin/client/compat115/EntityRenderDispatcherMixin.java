package virtuoel.pehkui.mixin.client.compat115;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleRenderUtils;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin
{
	@Dynamic
	@ModifyConstant(method = MixinConstants.RENDER_SHADOW_PART, constant = @Constant(doubleValue = 0.015625D))
	private static double pehkui$renderShadowPart$shadowHeight(double value)
	{
		return value - 0.0155D;
	}
	
	@Inject(method = "renderHitbox", at = @At(value = "TAIL"))
	private void pehkui$renderHitbox(MatrixStack matrices, VertexConsumer vertices, Entity entity, float f, CallbackInfo ci)
	{
		final float interactionWidth = ScaleUtils.getInteractionBoxWidthScale(entity);
		final float interactionHeight = ScaleUtils.getInteractionBoxHeightScale(entity);
		final float margin = entity.getTargetingMargin();
		
		if (interactionWidth != 1.0F || interactionHeight != 1.0F || margin != 0.0F)
		{
			Box bounds = entity.getBoundingBox();
			
			final double scaledXLength = bounds.getLengthX() * 0.5D * (interactionWidth - 1.0F);
			final double scaledYLength = bounds.getLengthY() * 0.5D * (interactionHeight - 1.0F);
			final double scaledZLength = bounds.getLengthZ() * 0.5D * (interactionWidth - 1.0F);
			final double scaledMarginWidth = margin * interactionWidth;
			final double scaledMarginHeight = margin * interactionHeight;
			
			bounds = bounds.expand(scaledXLength + scaledMarginWidth, scaledYLength + scaledMarginHeight, scaledZLength + scaledMarginWidth)
				.offset(-entity.getX(), -entity.getY(), -entity.getZ());
			
			ScaleRenderUtils.renderInteractionBox(matrices, vertices, bounds);
		}
	}
}
