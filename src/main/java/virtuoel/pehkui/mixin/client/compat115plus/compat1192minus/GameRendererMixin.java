package virtuoel.pehkui.mixin.client.compat115plus.compat1192minus;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleRenderUtils;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(GameRenderer.class)
public class GameRendererMixin
{
	@Shadow @Final @Mutable
	MinecraftClient client;
	
	@Dynamic
	@ModifyExpressionValue(method = MixinConstants.GET_BASIC_PROJECTION_MATRIX, at = @At(value = "CONSTANT", args = "floatValue=0.05F"))
	private float pehkui$getBasicProjectionMatrix$depth(float value)
	{
		return ScaleRenderUtils.modifyProjectionMatrixDepth(value, client.getCameraEntity(), client.getTickDelta());
	}
	
	@WrapOperation(method = "bobView", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(DDD)V"))
	private void pehkui$bobView$translate(double x, double y, double z, Operation<Void> original)
	{
		final float scale = ScaleUtils.getViewBobbingScale(client.getCameraEntity(), client.getTickDelta());
		
		if (scale != 1.0F)
		{
			x *= scale;
			y *= scale;
		}
		
		original.call(x, y, z);
	}
}
