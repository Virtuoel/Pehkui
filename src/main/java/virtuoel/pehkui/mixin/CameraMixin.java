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
	@Shadow abstract double clipToSpace(double distance);
	@Shadow abstract void moveBy(double x, double y, double z);
	
	@Redirect(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;clipToSpace(D)D"))
	public double onUpdateClipToSpaceProxy(Camera obj, double distance)
	{
		final float scale = ((ResizableEntity) focusedEntity).getScale(MinecraftClient.getInstance().getTickDelta());
		return clipToSpace(distance * scale);
	}
}
