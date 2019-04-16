package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import virtuoel.pehkui.api.ResizableEntity;

@Mixin(GameRenderer.class)
public class GameRendererMixin
{
	@Redirect(method = "bobView", at = @At(value = "INVOKE", target = "com/mojang/blaze3d/platform/GlStateManager.translatef(FFF)V", remap = false))
	public void bobViewTranslatefProxy(float x, float y, float z)
	{
		final MinecraftClient mc = MinecraftClient.getInstance();
		final float scale = ((ResizableEntity) mc.getCameraEntity()).getScale(mc.getTickDelta());
		GlStateManager.translatef(x * scale, y * scale, z * scale);
	}
}
