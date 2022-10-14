package virtuoel.pehkui.mixin.client.compat117plus;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin
{
	@Inject(method = "renderHitbox", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;drawBox(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/util/math/Box;FFFF)V", ordinal = 0))
	private static void pehkui$renderHitbox$renderInteractionHitbox(MatrixStack matrices, VertexConsumer vertices, Entity entity, float tickDelta, CallbackInfo ci)
	{
		final float interactionWidth = ScaleUtils.getInteractionWidthScale(entity);
		final float interactionHeight = ScaleUtils.getInteractionHeightScale(entity);

		if (interactionWidth != 1.0F || interactionHeight != 1.0F)
		{
			final double scaledWidth = (entity.getWidth() * interactionWidth * 0.30000001192092896D) - (entity.getWidth() * 0.30000001192092896D);
			final double scaledHeight = (entity.getHeight() * interactionHeight * 0.30000001192092896D) - (entity.getHeight() * 0.30000001192092896D);

			WorldRenderer.drawBox(matrices, vertices, entity.getBoundingBox().expand(scaledWidth, scaledHeight, scaledWidth).offset(-entity.getX(), -entity.getY(), -entity.getZ()), 1.0f, 0.0f, 1.0f, 1.0f);
		}
	}
}
