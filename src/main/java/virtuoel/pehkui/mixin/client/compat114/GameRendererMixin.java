package virtuoel.pehkui.mixin.client.compat114;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleRenderUtils;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(GameRenderer.class)
public class GameRendererMixin
{
	@Shadow @Final @Mutable
	MinecraftClient client;
	
	@Dynamic
	@ModifyExpressionValue(method = MixinConstants.APPLY_CAMERA_TRANSFORMATIONS, at = @At(value = "CONSTANT", args = "floatValue=0.05F"))
	private float pehkui$applyCameraTransformations$depth(float value)
	{
		return ScaleRenderUtils.modifyProjectionMatrixDepthByWidth(value, client.getCameraEntity(), client.getTickDelta());
	}
	
	@Unique
	boolean pehkui$isBobbing = false;
	
	@Dynamic
	@Inject(method = MixinConstants.APPLY_CAMERA_TRANSFORMATIONS, at = @At(value = "INVOKE", shift = Shift.BEFORE, target = MixinConstants.BOB_VIEW))
	private void pehkui$renderWorld$before(float tickDelta, CallbackInfo info)
	{
		pehkui$isBobbing = true;
	}
	
	@Dynamic
	@Inject(method = MixinConstants.APPLY_CAMERA_TRANSFORMATIONS, at = @At(value = "INVOKE", shift = Shift.AFTER, target = MixinConstants.BOB_VIEW))
	private void pehkui$renderWorld$after(float tickDelta, CallbackInfo info)
	{
		pehkui$isBobbing = false;
	}
	
	@Dynamic
	@WrapOperation(method = MixinConstants.BOB_VIEW, at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlStateManager;translatef(FFF)V", remap = false))
	private void pehkui$bobView$translate(float x, float y, float z, Operation<Void> original)
	{
		if (pehkui$isBobbing)
		{
			final float scale = ScaleUtils.getViewBobbingScale(client.getCameraEntity(), client.getTickDelta());
			
			if (scale != 1.0F)
			{
				x *= scale;
				y *= scale;
				z *= scale;
			}
		}
		
		original.call(x, y, z);
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
