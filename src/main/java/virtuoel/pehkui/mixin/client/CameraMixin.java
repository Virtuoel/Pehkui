package virtuoel.pehkui.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(Camera.class)
public abstract class CameraMixin
{
	@Shadow Entity focusedEntity;
	
	@ModifyVariable(method = "clipToSpace", at = @At(value = "HEAD"), argsOnly = true)
	private double pehkui$clipToSpace(double desiredCameraDistance)
	{
		return desiredCameraDistance * ScaleUtils.getThirdPersonScale(focusedEntity, MinecraftClient.getInstance().getTickDelta());
	}
	
	@ModifyExpressionValue(method = "clipToSpace", at = @At(value = "CONSTANT", args = "floatValue=0.1F"))
	private float pehkui$clipToSpace$offset(float value)
	{
		final float scale = ScaleUtils.getBoundingBoxWidthScale(focusedEntity);
		
		return scale < 1.0F ? scale * value : value;
	}
}
