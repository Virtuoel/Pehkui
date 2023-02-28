package virtuoel.pehkui.mixin.client.compat115plus;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(GameRenderer.class)
public class GameRendererMixin
{
	@Shadow @Final @Mutable
	MinecraftClient client;
	
	@Redirect(method = "bobView", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerEntity;horizontalSpeed:F"))
	private float pehkui$bobView$horizontalSpeed(PlayerEntity obj)
	{
		final float scale = ScaleUtils.getViewBobbingScale(obj, 1.0F);
		return scale != 1.0F ? ScaleUtils.divideClamped(obj.horizontalSpeed, scale) : obj.horizontalSpeed;
	}
	
	@Redirect(method = "bobView", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerEntity;prevHorizontalSpeed:F"))
	private float pehkui$bobView$prevHorizontalSpeed(PlayerEntity obj)
	{
		final float scale = ScaleUtils.getViewBobbingScale(obj, 0.0F);
		return scale != 1.0F ? ScaleUtils.divideClamped(obj.prevHorizontalSpeed, scale) : obj.prevHorizontalSpeed;
	}
	
	@ModifyArg(method = "bobView", index = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V"))
	private float pehkui$bobView$translate$x(float value)
	{
		final float scale = ScaleUtils.getViewBobbingScale(client.getCameraEntity(), client.getTickDelta());
		return scale != 1.0F ? value * scale : value;
	}
	
	@ModifyArg(method = "bobView", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V"))
	private float pehkui$bobView$translate$y(float value)
	{
		final float scale = ScaleUtils.getViewBobbingScale(client.getCameraEntity(), client.getTickDelta());
		return scale != 1.0F ? value * scale : value;
	}
}
