package virtuoel.pehkui.mixin.client.compat114;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.entity.BoatEntityRenderer;
import net.minecraft.client.render.entity.model.BoatEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.BoatEntity;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(BoatEntityRenderer.class)
public abstract class BoatEntityRendererMixin
{
	@Inject(target = @Desc(value = MixinConstants.RENDER_SECOND_PASS, args = { BoatEntity.class, double.class, double.class, double.class, float.class, float.class }), at = @At(value = "INVOKE", shift = Shift.BEFORE, desc = @Desc(value = MixinConstants.RENDER_PASS, owner = BoatEntityModel.class, args = { Entity.class, float.class, float.class, float.class, float.class, float.class, float.class }), remap = false), remap = false)
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
	
	@Inject(target = @Desc(value = MixinConstants.RENDER_SECOND_PASS, args = { BoatEntity.class, double.class, double.class, double.class, float.class, float.class }), at = @At(value = "INVOKE", shift = Shift.AFTER, desc = @Desc(value = MixinConstants.RENDER_PASS, owner = BoatEntityModel.class, args = { Entity.class, float.class, float.class, float.class, float.class, float.class, float.class }), remap = false), remap = false)
	private void onRenderPostRender(BoatEntity boatEntity, double x, double y, double z, float yaw, float tickDelta, CallbackInfo info)
	{
		GL11.glPopMatrix();
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}
}
