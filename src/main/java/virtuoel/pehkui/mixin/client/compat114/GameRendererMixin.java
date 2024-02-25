package virtuoel.pehkui.mixin.client.compat114;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

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
	
	@Dynamic
	@WrapOperation(method = MixinConstants.BOB_VIEW, at = @At(value = "FIELD", target = MixinConstants.HORIZONTAL_SPEED))
	private float pehkui$bobView$horizontalSpeed(PlayerEntity obj, Operation<Float> original)
	{
		final float scale = ScaleUtils.getViewBobbingScale(obj, 1.0F);
		return scale != 1.0F ? ScaleUtils.divideClamped(original.call(obj), scale) : original.call(obj);
	}
	
	@Dynamic
	@WrapOperation(method = MixinConstants.BOB_VIEW, at = @At(value = "FIELD", target = MixinConstants.PREV_HORIZONTAL_SPEED))
	private float pehkui$bobView$prevHorizontalSpeed(PlayerEntity obj, Operation<Float> original)
	{
		final float scale = ScaleUtils.getViewBobbingScale(obj, 0.0F);
		return scale != 1.0F ? ScaleUtils.divideClamped(original.call(obj), scale) : original.call(obj);
	}
	
	@Dynamic
	@ModifyArg(method = MixinConstants.BOB_VIEW, index = 0, at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlStateManager;translatef(FFF)V", remap = false))
	private float pehkui$bobView$translate$x(float value)
	{
		final float scale = ScaleUtils.getViewBobbingScale(client.getCameraEntity(), client.getTickDelta());
		return scale != 1.0F ? value * scale : value;
	}
	
	@Dynamic
	@ModifyArg(method = MixinConstants.BOB_VIEW, index = 1, at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlStateManager;translatef(FFF)V", remap = false))
	private float pehkui$bobView$translate$y(float value)
	{
		final float scale = ScaleUtils.getViewBobbingScale(client.getCameraEntity(), client.getTickDelta());
		return scale != 1.0F ? value * scale : value;
	}
	
	@Dynamic
	@ModifyExpressionValue(method = MixinConstants.APPLY_CAMERA_TRANSFORMATIONS, at = @At(value = "CONSTANT", args = "floatValue=0.05F"))
	private float pehkui$applyCameraTransformations$depth(float value)
	{
		return ScaleRenderUtils.modifyProjectionMatrixDepthByWidth(value, client.getCameraEntity(), client.getTickDelta());
	}
	
	@Dynamic
	@ModifyExpressionValue(method = MixinConstants.RENDER_HAND, at = @At(value = "CONSTANT", args = "floatValue=0.05F"))
	private float pehkui$renderHand$depth(float value)
	{
		return ScaleRenderUtils.modifyProjectionMatrixDepthByWidth(value, client.getCameraEntity(), client.getTickDelta());
	}
	
	@Dynamic
	@ModifyExpressionValue(method = MixinConstants.RENDER_CENTER, at = @At(value = "CONSTANT", args = "floatValue=0.05F"))
	private float pehkui$renderCenter$depth(float value)
	{
		return ScaleRenderUtils.modifyProjectionMatrixDepthByWidth(value, client.getCameraEntity(), client.getTickDelta());
	}
	
	@Dynamic
	@ModifyExpressionValue(method = MixinConstants.RENDER_ABOVE_CLOUDS, at = @At(value = "CONSTANT", args = "floatValue=0.05F"))
	private float pehkui$renderAboveClouds$depth(float value)
	{
		return ScaleRenderUtils.modifyProjectionMatrixDepthByHeight(value, client.getCameraEntity(), client.getTickDelta());
	}
}
