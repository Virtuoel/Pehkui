package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import virtuoel.pehkui.api.ResizableEntity;

@Mixin(Camera.class)
public abstract class CameraMixin
{
	@Shadow Entity focusedEntity;
	@Shadow abstract double method_19318(double distance);
	
	@Redirect(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;method_19318(D)D"))
	public double onUpdateMethod_19318Proxy(Camera obj, double distance)
	{
		final float scale = ((ResizableEntity) focusedEntity).getScaleLerp(MinecraftClient.getInstance().getTickDelta());
		return method_19318(distance * scale);
	}
}
