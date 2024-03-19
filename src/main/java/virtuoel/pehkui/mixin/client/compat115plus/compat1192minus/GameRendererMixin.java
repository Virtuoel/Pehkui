package virtuoel.pehkui.mixin.client.compat115plus.compat1192minus;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleRenderUtils;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(value = GameRenderer.class, priority = 990)
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
	
	@Unique
	boolean pehkui$isBobbing = false;
	
	@Inject(method = "renderWorld", at = @At(value = "INVOKE", shift = Shift.BEFORE, target = "Lnet/minecraft/client/render/GameRenderer;bobView(Lnet/minecraft/client/util/math/MatrixStack;F)V"))
	private void pehkui$renderWorld$before(float tickDelta, long limitTime, MatrixStack matrices, CallbackInfo info)
	{
		pehkui$isBobbing = true;
	}
	
	@Inject(method = "renderWorld", at = @At(value = "INVOKE", shift = Shift.AFTER, target = "Lnet/minecraft/client/render/GameRenderer;bobView(Lnet/minecraft/client/util/math/MatrixStack;F)V"))
	private void pehkui$renderWorld$after(float tickDelta, long limitTime, MatrixStack matrices, CallbackInfo info)
	{
		pehkui$isBobbing = false;
	}
	
	@WrapOperation(method = "bobView", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(DDD)V"))
	private void pehkui$bobView$translate(MatrixStack obj, double x, double y, double z, Operation<Void> original)
	{
		if (pehkui$isBobbing)
		{
			final float scale = ScaleUtils.getViewBobbingScale(client.getCameraEntity(), client.getTickDelta());
			
			if (scale != 1.0F)
			{
				x *= scale;
				y *= scale;
				z *= scale;
			}
		}
		
		original.call(obj, x, y, z);
	}
}
