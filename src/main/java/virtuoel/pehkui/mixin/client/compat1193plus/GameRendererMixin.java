package virtuoel.pehkui.mixin.client.compat1193plus;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import virtuoel.pehkui.util.ScaleRenderUtils;

@Mixin(GameRenderer.class)
public class GameRendererMixin
{
	@Shadow @Final @Mutable
	MinecraftClient client;
	
	@ModifyExpressionValue(method = "getBasicProjectionMatrix(D)Lorg/joml/Matrix4f;", at = @At(value = "CONSTANT", args = "floatValue=0.05F"))
	private float pehkui$getBasicProjectionMatrix$depth(float value)
	{
		return ScaleRenderUtils.modifyProjectionMatrixDepth(value, client.getCameraEntity(), client.getTickDelta());
	}
}
