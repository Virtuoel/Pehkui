package virtuoel.pehkui.mixin.client.compat114;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.entity.ShulkerEntityRenderer;
import net.minecraft.entity.mob.ShulkerEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ShulkerEntityRenderer.class)
public class ShulkerEntityRendererMixin
{
	@Inject(at = @At("RETURN"), method = "method_4058", remap = false)
	private void onSetupTransforms(ShulkerEntity entity, float animationProgress, float bodyYaw, float tickDelta, CallbackInfo info)
	{
		final float w = ScaleUtils.getModelWidthScale(entity, tickDelta);
		final float h = ScaleUtils.getModelHeightScale(entity, tickDelta);
		
		switch (entity.getAttachedFace())
		{
			case NORTH:
			case SOUTH:
			case EAST:
			case WEST:
				GL11.glTranslated(0.0, -((1.0F - w) * 0.5F) / w, -((1.0F - h) * 0.5F) / h);
				break;
			case UP:
				GL11.glTranslated(0.0, -(1.0F - h) / h, 0.0);
				break;
			default:
				break;
		}
	}
}
