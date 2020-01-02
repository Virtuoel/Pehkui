package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerEntity;
import virtuoel.pehkui.api.ScaleData;

@Mixin(GameRenderer.class)
public class GameRendererMixin
{
	@Shadow
	MinecraftClient client;
	/* // TODO FIXME
	@Redirect(method = "bobView", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerEntity;horizontalSpeed:F"))
	private float bobViewHorizontalSpeedProxy(PlayerEntity obj)
	{
		final float scale = ScaleData.of(obj).getScale(client.getTickDelta());
		return obj.horizontalSpeed * scale;
	}
	
	@Redirect(method = "bobView", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerEntity;prevHorizontalSpeed:F"))
	private float bobViewPrevHorizontalSpeedProxy(PlayerEntity obj)
	{
		final float scale = ScaleData.of(obj).getScale(client.getTickDelta());
		return obj.prevHorizontalSpeed * scale;
	}
	
	@Redirect(method = "bobView", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerEntity;field_7505:F"))
	private float bobViewField_7505Proxy(PlayerEntity obj)
	{
		final float scale = ScaleData.of(obj).getScale(client.getTickDelta());
		return obj.field_7505 * scale;
	}
	
	@Redirect(method = "bobView", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerEntity;field_7483:F"))
	private float bobViewField_7483Proxy(PlayerEntity obj)
	{
		final float scale = ScaleData.of(obj).getScale(client.getTickDelta());
		return obj.field_7483 * scale;
	}*/
}
