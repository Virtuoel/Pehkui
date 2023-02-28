package virtuoel.pehkui.mixin.client.compat114;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerEntity;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleRenderUtils;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(GameRenderer.class)
public class GameRendererMixin
{
	@Shadow @Final @Mutable
	MinecraftClient client;
	
	@Redirect(method = MixinConstants.BOB_VIEW, at = @At(value = "FIELD", target = MixinConstants.HORIZONTAL_SPEED, remap = false), remap = false)
	private float pehkui$bobView$horizontalSpeed(PlayerEntity obj)
	{
		final float scale = ScaleUtils.getViewBobbingScale(obj, 1.0F);
		return scale != 1.0F ? ScaleUtils.divideClamped(obj.horizontalSpeed, scale) : obj.horizontalSpeed;
	}
	
	@Redirect(method = MixinConstants.BOB_VIEW, at = @At(value = "FIELD", target = MixinConstants.PREV_HORIZONTAL_SPEED, remap = false), remap = false)
	private float pehkui$bobView$prevHorizontalSpeed(PlayerEntity obj)
	{
		final float scale = ScaleUtils.getViewBobbingScale(obj, 0.0F);
		return scale != 1.0F ? ScaleUtils.divideClamped(obj.prevHorizontalSpeed, scale) : obj.prevHorizontalSpeed;
	}
	
	@ModifyArg(method = MixinConstants.BOB_VIEW, index = 0, at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlStateManager;translatef(FFF)V", remap = false), remap = false)
	private float pehkui$bobView$translate$x(float value)
	{
		final float scale = ScaleUtils.getViewBobbingScale(client.getCameraEntity(), client.getTickDelta());
		return scale != 1.0F ? value * scale : value;
	}
	
	@ModifyArg(method = MixinConstants.BOB_VIEW, index = 1, at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlStateManager;translatef(FFF)V", remap = false), remap = false)
	private float pehkui$bobView$translate$y(float value)
	{
		final float scale = ScaleUtils.getViewBobbingScale(client.getCameraEntity(), client.getTickDelta());
		return scale != 1.0F ? value * scale : value;
	}
	
	@ModifyConstant(method = MixinConstants.APPLY_CAMERA_TRANSFORMATIONS, constant = @Constant(floatValue = 0.05F), remap = false)
	private float pehkui$applyCameraTransformations$depth(float value)
	{
		return ScaleRenderUtils.modifyProjectionMatrixDepthByWidth(value, client.getCameraEntity(), client.getTickDelta());
	}
	
	@ModifyConstant(method = MixinConstants.RENDER_HAND, constant = @Constant(floatValue = 0.05F), remap = false)
	private float pehkui$renderHand$depth(float value)
	{
		return ScaleRenderUtils.modifyProjectionMatrixDepthByWidth(value, client.getCameraEntity(), client.getTickDelta());
	}
	
	@ModifyConstant(method = MixinConstants.RENDER_CENTER, constant = @Constant(floatValue = 0.05F), remap = false)
	private float pehkui$renderCenter$depth(float value)
	{
		return ScaleRenderUtils.modifyProjectionMatrixDepthByWidth(value, client.getCameraEntity(), client.getTickDelta());
	}
	
	@ModifyConstant(method = MixinConstants.RENDER_ABOVE_CLOUDS, constant = @Constant(floatValue = 0.05F), remap = false)
	private float pehkui$renderAboveClouds$depth(float value)
	{
		return ScaleRenderUtils.modifyProjectionMatrixDepthByHeight(value, client.getCameraEntity(), client.getTickDelta());
	}
}
