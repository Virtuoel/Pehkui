package virtuoel.pehkui.mixin.client.compat114;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.player.PlayerEntity;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(GameRenderer.class)
public class GameRendererMixin
{
	@Shadow @Final @Mutable
	MinecraftClient client;
	
	@Redirect(target = @Desc(value = MixinConstants.BOB_VIEW, args = { float.class }), at = @At(value = "FIELD", target = MixinConstants.HORIZONTAL_SPEED, remap = false), remap = false)
	private float bobViewHorizontalSpeedProxy(PlayerEntity obj)
	{
		final float scale = ScaleUtils.getViewBobbingScale(obj, client.getTickDelta());
		return obj.horizontalSpeed / scale;
	}
	
	@Redirect(target = @Desc(value = MixinConstants.BOB_VIEW, args = { float.class }), at = @At(value = "FIELD", target = MixinConstants.PREV_HORIZONTAL_SPEED, remap = false), remap = false)
	private float bobViewPrevHorizontalSpeedProxy(PlayerEntity obj)
	{
		final float scale = ScaleUtils.getViewBobbingScale(obj, client.getTickDelta());
		return obj.prevHorizontalSpeed / scale;
	}
	
	@ModifyConstant(target = @Desc(value = MixinConstants.APPLY_CAMERA_TRANSFORMATIONS, args = { float.class }), constant = @Constant(floatValue = 0.05F), remap = false)
	private float applyCameraTransformationsModifyDepth(float value)
	{
		return ScaleUtils.modifyProjectionMatrixDepthByWidth(value, client.getCameraEntity(), client.getTickDelta());
	}
	
	@ModifyConstant(target = @Desc(value = MixinConstants.RENDER_HAND, args = { Camera.class, float.class }), constant = @Constant(floatValue = 0.05F), remap = false)
	private float renderHandModifyDepth(float value)
	{
		return ScaleUtils.modifyProjectionMatrixDepthByWidth(value, client.getCameraEntity(), client.getTickDelta());
	}
	
	@ModifyConstant(target = @Desc(value = MixinConstants.RENDER_CENTER, args = { float.class, long.class }), constant = @Constant(floatValue = 0.05F), remap = false)
	private float renderCenterModifyDepth(float value)
	{
		return ScaleUtils.modifyProjectionMatrixDepthByWidth(value, client.getCameraEntity(), client.getTickDelta());
	}
	
	@ModifyConstant(target = @Desc(value = MixinConstants.RENDER_ABOVE_CLOUDS, args = { Camera.class, WorldRenderer.class, float.class, double.class, double.class, double.class }), constant = @Constant(floatValue = 0.05F), remap = false)
	private float renderAboveCloudsModifyDepth(float value)
	{
		return ScaleUtils.modifyProjectionMatrixDepthByHeight(value, client.getCameraEntity(), client.getTickDelta());
	}
}
