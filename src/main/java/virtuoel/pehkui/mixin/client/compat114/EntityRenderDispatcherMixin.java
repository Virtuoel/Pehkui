package virtuoel.pehkui.mixin.client.compat114;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin
{
	@Inject(method = MixinConstants.RENDER, at = @At(value = "INVOKE", shift = Shift.BEFORE, target = MixinConstants.RENDER_IN_WORLD, remap = false), remap = false)
	private <E extends Entity> void onRenderPreRender(E entity, double x, double y, double z, float yaw, float tickDelta, boolean forceHideHitbox, CallbackInfo info)
	{
		final float widthScale = ScaleUtils.getModelWidthScale(entity, tickDelta);
		final float heightScale = ScaleUtils.getModelHeightScale(entity, tickDelta);
		
		GL11.glPushMatrix();
		GL11.glScalef(widthScale, heightScale, widthScale);
		GL11.glTranslated((x / widthScale) - x, (y / heightScale) - y, (z / widthScale) - z);
		GL11.glPushMatrix();
	}
	
	@Inject(method = MixinConstants.RENDER, at = @At(value = "INVOKE", shift = Shift.AFTER, target = MixinConstants.RENDER_IN_WORLD, remap = false), remap = false)
	private <E extends Entity> void onRenderPostRender(E entity, double x, double y, double z, float yaw, float tickDelta, boolean forceHideHitbox, CallbackInfo info)
	{
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}
}
