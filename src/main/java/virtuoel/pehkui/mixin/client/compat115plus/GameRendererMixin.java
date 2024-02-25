package virtuoel.pehkui.mixin.client.compat115plus;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(GameRenderer.class)
public class GameRendererMixin
{
	@Shadow @Final @Mutable
	MinecraftClient client;
	
	@WrapOperation(method = "bobView", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerEntity;horizontalSpeed:F"))
	private float pehkui$bobView$horizontalSpeed(PlayerEntity obj, Operation<Float> original)
	{
		final float scale = ScaleUtils.getViewBobbingScale(obj, 1.0F);
		return scale != 1.0F ? ScaleUtils.divideClamped(original.call(obj), scale) : original.call(obj);
	}
	
	@WrapOperation(method = "bobView", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerEntity;prevHorizontalSpeed:F"))
	private float pehkui$bobView$prevHorizontalSpeed(PlayerEntity obj, Operation<Float> original)
	{
		final float scale = ScaleUtils.getViewBobbingScale(obj, 0.0F);
		return scale != 1.0F ? ScaleUtils.divideClamped(original.call(obj), scale) : original.call(obj);
	}
}
