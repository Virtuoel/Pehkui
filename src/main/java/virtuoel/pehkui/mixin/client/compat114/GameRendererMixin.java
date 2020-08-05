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
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(GameRenderer.class)
public class GameRendererMixin
{
	@Shadow @Final @Mutable
	MinecraftClient client;
	
	@Redirect(method = "method_3186(F)V", at = @At(value = "FIELD", target = "Lnet/minecraft/class_1657;field_5973:F", remap = false), remap = false)
	private float bobViewHorizontalSpeedProxy(PlayerEntity obj)
	{
		final float scale = ScaleUtils.getMotionScale(obj, client.getTickDelta());
		return obj.horizontalSpeed / scale;
	}
	
	@Redirect(method = "method_3186(F)V", at = @At(value = "FIELD", target = "Lnet/minecraft/class_1657;field_6039:F", remap = false), remap = false)
	private float bobViewPrevHorizontalSpeedProxy(PlayerEntity obj)
	{
		final float scale = ScaleUtils.getMotionScale(obj, client.getTickDelta());
		return obj.prevHorizontalSpeed / scale;
	}
	
	@ModifyConstant(method = "method_3185(F)V", constant = @Constant(floatValue = 0.05F), remap = false)
	private float applyCameraTransformationsModifyDepth(float value)
	{
		final Entity entity = client.getCameraEntity();
		
		if (entity != null)
		{
			final float scale = ScaleUtils.getWidthScale(entity, client.getTickDelta());
			
			if (scale < 1.0F)
			{
				return Math.max(0.001F, value * scale);
			}
		}
		
		return value;
	}
	
	@ModifyConstant(method = "method_3172(Lnet/minecraft/class_4184;F)V", constant = @Constant(floatValue = 0.05F), remap = false)
	private float renderHandModifyDepth(float value)
	{
		final Entity entity = client.getCameraEntity();
		
		if (entity != null)
		{
			final float scale = ScaleUtils.getWidthScale(entity, client.getTickDelta());
			
			if (scale < 1.0F)
			{
				return Math.max(0.001F, value * scale);
			}
		}
		
		return value;
	}
	
	@ModifyConstant(method = "method_3178(FJ)V", constant = @Constant(floatValue = 0.05F), remap = false)
	private float renderCenterModifyDepth(float value)
	{
		final Entity entity = client.getCameraEntity();
		
		if (entity != null)
		{
			final float scale = ScaleUtils.getWidthScale(entity, client.getTickDelta());
			
			if (scale < 1.0F)
			{
				return Math.max(0.001F, value * scale);
			}
		}
		
		return value;
	}
	
	@ModifyConstant(method = "method_3206(Lnet/minecraft/class_4184;Lnet/minecraft/class_761;FDDD)V", constant = @Constant(floatValue = 0.05F), remap = false)
	private float renderAboveCloudsModifyDepth(float value)
	{
		final Entity entity = client.getCameraEntity();
		
		if (entity != null)
		{
			final float scale = ScaleUtils.getHeightScale(entity, client.getTickDelta());
			
			if (scale < 1.0F)
			{
				return Math.max(0.001F, value * scale);
			}
		}
		
		return value;
	}
}
