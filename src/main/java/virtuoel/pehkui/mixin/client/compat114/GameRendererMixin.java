package virtuoel.pehkui.mixin.client.compat114;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
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
	
	@Dynamic
	@Inject(method = MixinConstants.APPLY_CAMERA_TRANSFORMATIONS, at = @At(value = "INVOKE", target = MixinConstants.BOB_VIEW))
	private void pehkui$renderWorld(float tickDelta, CallbackInfo info)
	{
		final float scale = ScaleUtils.getViewBobbingScale(client.getCameraEntity(), client.getTickDelta());
		
		if (scale != 1.0F)
		{
			final float multiplier = scale - 1.0F;
			
			final PlayerEntity playerEntity = (PlayerEntity) client.getCameraEntity();
			final float speedLerp = -(playerEntity.horizontalSpeed + ((playerEntity.horizontalSpeed - playerEntity.prevHorizontalSpeed) * tickDelta));
			final float strideLerp = MathHelper.lerp(tickDelta, playerEntity.prevStrideDistance, playerEntity.strideDistance);
			
			GL11.glTranslatef(multiplier * MathHelper.sin(speedLerp * (float) Math.PI) * strideLerp * 0.5F, multiplier * -Math.abs(MathHelper.cos(speedLerp * (float) Math.PI) * strideLerp), 0.0F);
		}
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
