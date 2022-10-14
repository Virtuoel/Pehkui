package virtuoel.pehkui.mixin.client.compat114;

import net.minecraft.util.math.Box;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import virtuoel.pehkui.mixin.compat114.EntityAccessor;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleRenderUtils;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin
{
	@Inject(method = MixinConstants.RENDER, at = @At(value = "INVOKE", shift = Shift.BEFORE, target = MixinConstants.RENDER_IN_WORLD, remap = false), remap = false)
	private <E extends Entity> void pehkui$render$before(E entity, double x, double y, double z, float yaw, float tickDelta, boolean forceHideHitbox, CallbackInfo info)
	{
		ScaleRenderUtils.logIfEntityRenderCancelled();
		
		final float widthScale = ScaleUtils.getModelWidthScale(entity, tickDelta);
		final float heightScale = ScaleUtils.getModelHeightScale(entity, tickDelta);
		
		GL11.glPushMatrix();
		GL11.glScalef(widthScale, heightScale, widthScale);
		GL11.glTranslated((x / widthScale) - x, (y / heightScale) - y, (z / widthScale) - z);
		GL11.glPushMatrix();
		
		ScaleRenderUtils.saveLastRenderedEntity(entity.getType());
	}
	
	@Inject(method = MixinConstants.RENDER, at = @At(value = "INVOKE", shift = Shift.AFTER, target = MixinConstants.RENDER_IN_WORLD, remap = false), remap = false)
	private <E extends Entity> void pehkui$render$after(E entity, double x, double y, double z, float yaw, float tickDelta, boolean forceHideHitbox, CallbackInfo info)
	{
		ScaleRenderUtils.clearLastRenderedEntity();
		
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}

	@Inject(method = MixinConstants.RENDER_HITBOX, at = @At(value = "INVOKE", target = MixinConstants.TESSELATOR_GET_INSTANCE, remap = false), remap = false)
	private void pehkui$renderHitbox$renderInteractionHitbox(Entity entity, double d, double e, double f, float g, float h, CallbackInfo ci)
	{
		final float interactionWidth = ScaleUtils.getInteractionWidthScale(entity);
		final float interactionHeight = ScaleUtils.getInteractionHeightScale(entity);

		if (interactionWidth != 1.0F || interactionHeight != 1.0F)
		{
			final double scaledWidth = (entity.getWidth() * interactionWidth * 0.30000001192092896D) - (entity.getWidth() * 0.30000001192092896D);
			final double scaledHeight = (entity.getHeight() * interactionHeight * 0.30000001192092896D) - (entity.getHeight() * 0.30000001192092896D);

			final Box box = entity.getBoundingBox().expand(scaledWidth, scaledHeight, scaledWidth).offset(-((EntityAccessor)entity).pehkui$getX() + d, -((EntityAccessor) entity).pehkui$getY() + e, -((EntityAccessor) entity).pehkui$getZ() + f);

			WorldRendererAccessor.pehkui$drawBoxOutline(box, 1.0F, 0.0F, 1.0F, 1.0F);
		}
	}
}
