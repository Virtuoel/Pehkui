package virtuoel.pehkui.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(Camera.class)
public abstract class CameraMixin
{
	@Shadow Entity focusedEntity;
	
	@ModifyArg(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;clipToSpace(D)D"))
	private double onUpdateClipToSpaceProxy(double distance)
	{
		return distance * ScaleUtils.getThirdPersonScale(focusedEntity, MinecraftClient.getInstance().getTickDelta());
	}
	
	@ModifyConstant(method = "clipToSpace", constant = @Constant(floatValue = 0.1F))
	private float clipToSpaceModifyOffset(float value)
	{
		final float scale = ScaleUtils.getBoundingBoxWidthScale(focusedEntity);
		
		return scale < 1.0F ? scale * value : value;
	}
}
