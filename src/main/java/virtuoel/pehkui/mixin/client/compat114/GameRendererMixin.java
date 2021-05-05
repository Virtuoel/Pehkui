package virtuoel.pehkui.mixin.client.compat114;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerEntity;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(GameRenderer.class)
public class GameRendererMixin
{
	@Shadow @Final @Mutable
	MinecraftClient client;
	
	@Redirect(method = MixinConstants.BOB_VIEW, at = @At(value = "FIELD", target = MixinConstants.HORIZONTAL_SPEED, remap = false), remap = false)
	private float bobViewHorizontalSpeedProxy(PlayerEntity obj)
	{
		final float scale = ScaleUtils.getMotionScale(obj, client.getTickDelta());
		return obj.horizontalSpeed / scale;
	}
	
	@Redirect(method = MixinConstants.BOB_VIEW, at = @At(value = "FIELD", target = MixinConstants.PREV_HORIZONTAL_SPEED, remap = false), remap = false)
	private float bobViewPrevHorizontalSpeedProxy(PlayerEntity obj)
	{
		final float scale = ScaleUtils.getMotionScale(obj, client.getTickDelta());
		return obj.prevHorizontalSpeed / scale;
	}
	
	@ModifyConstant(method = MixinConstants.APPLY_CAMERA_TRANSFORMATIONS, constant = @Constant(floatValue = 0.05F), remap = false)
	private float applyCameraTransformationsModifyDepth(float value)
	{
		return ScaleUtils.modifyProjectionMatrixDepthByWidth(value, client.getCameraEntity(), client.getTickDelta());
	}
	
	@ModifyConstant(method = MixinConstants.RENDER_HAND, constant = @Constant(floatValue = 0.05F), remap = false)
	private float renderHandModifyDepth(float value)
	{
		return ScaleUtils.modifyProjectionMatrixDepthByWidth(value, client.getCameraEntity(), client.getTickDelta());
	}
	
	@ModifyConstant(method = MixinConstants.RENDER_CENTER, constant = @Constant(floatValue = 0.05F), remap = false)
	private float renderCenterModifyDepth(float value)
	{
		return ScaleUtils.modifyProjectionMatrixDepthByWidth(value, client.getCameraEntity(), client.getTickDelta());
	}
	
	@ModifyConstant(method = MixinConstants.RENDER_ABOVE_CLOUDS, constant = @Constant(floatValue = 0.05F), remap = false)
	private float renderAboveCloudsModifyDepth(float value)
	{
		return ScaleUtils.modifyProjectionMatrixDepthByHeight(value, client.getCameraEntity(), client.getTickDelta());
	}
}
