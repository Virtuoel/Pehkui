package virtuoel.pehkui.mixin.client.compat116minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin
{
	@ModifyVariable(method = "renderStatusBars", at = @At(value = "INVOKE", shift = Shift.BEFORE, target = "Lnet/minecraft/entity/player/PlayerEntity;getAbsorptionAmount()F"))
	private float pehkui$renderStatusBars(float value)
	{
		final MinecraftClient client = MinecraftClient.getInstance();
		
		final float healthScale = ScaleUtils.getHealthScale(client.getCameraEntity(), client.getTickDelta());
		
		return healthScale != 1.0F ? value * healthScale : value;
	}
}
