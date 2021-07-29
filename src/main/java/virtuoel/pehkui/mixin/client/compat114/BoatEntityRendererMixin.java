package virtuoel.pehkui.mixin.client.compat114;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.entity.BoatEntityRenderer;
import net.minecraft.entity.vehicle.BoatEntity;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(BoatEntityRenderer.class)
public abstract class BoatEntityRendererMixin
{
	@Inject(method = MixinConstants.RENDER_SECOND_PASS, at = @At(value = "INVOKE", shift = Shift.BEFORE, target = MixinConstants.RENDER_PASS, remap = false), remap = false)
	private void onRenderPreRender(BoatEntity boatEntity, double x, double y, double z, float yaw, float tickDelta, CallbackInfo info)
	{
		final float widthScale = ScaleUtils.getModelWidthScale(boatEntity, tickDelta);
		final float heightScale = ScaleUtils.getModelHeightScale(boatEntity, tickDelta);
		
		GL11.glPushMatrix();
		GL11.glTranslatef(0.0F, 0.375F * (1.0F - heightScale), 0.0F);
		GL11.glPushMatrix();
		GL11.glScalef(widthScale, heightScale, widthScale);
		GL11.glPushMatrix();
	}
	
	@Inject(method = MixinConstants.RENDER_SECOND_PASS, at = @At(value = "INVOKE", shift = Shift.AFTER, target = MixinConstants.RENDER_PASS, remap = false), remap = false)
	private void onRenderPostRender(BoatEntity boatEntity, double x, double y, double z, float yaw, float tickDelta, CallbackInfo info)
	{
		GL11.glPopMatrix();
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}
}
