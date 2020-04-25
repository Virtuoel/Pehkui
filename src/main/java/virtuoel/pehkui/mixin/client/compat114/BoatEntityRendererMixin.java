package virtuoel.pehkui.mixin.client.compat114;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.entity.BoatEntityRenderer;
import net.minecraft.entity.vehicle.BoatEntity;
import virtuoel.pehkui.api.ScaleData;

@Mixin(BoatEntityRenderer.class)
public abstract class BoatEntityRendererMixin
{
	@Inject(method = "method_3887(Lnet/minecraft/class_1690;DDDFF)V", at = @At(value = "INVOKE", shift = Shift.BEFORE, target = "Lnet/minecraft/class_554;method_2836(Lnet/minecraft/class_1297;FFFFFF)V", remap = false), remap = false)
	private void onRenderPreRender(BoatEntity boatEntity, double x, double y, double z, float yaw, float tickDelta, CallbackInfo info)
	{
		final float scale = ScaleData.of(boatEntity).getScale(tickDelta);

		GL11.glPushMatrix();
		GL11.glTranslatef(0.0F, 0.375F * (1.0F - scale), 0.0F);
		GL11.glPushMatrix();
		GL11.glScalef(scale, scale, scale);
		GL11.glPushMatrix();
	}
	
	@Inject(method = "method_3887(Lnet/minecraft/class_1690;DDDFF)V", at = @At(value = "INVOKE", shift = Shift.AFTER, target = "Lnet/minecraft/class_554;method_2836(Lnet/minecraft/class_1297;FFFFFF)V", remap = false), remap = false)
	private void onRenderPostRender(BoatEntity boatEntity, double x, double y, double z, float yaw, float tickDelta, CallbackInfo info)
	{
		GL11.glPopMatrix();
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}
}
