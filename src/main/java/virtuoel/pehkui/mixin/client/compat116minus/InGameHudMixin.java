package virtuoel.pehkui.mixin.client.compat116minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin
{
	@ModifyVariable(method = MixinConstants.RENDER_STATUS_BARS, at = @At(value = "INVOKE", shift = Shift.BEFORE, target = MixinConstants.GET_ABSORPTION_AMOUNT))
	private float pehkui$renderStatusBars(float value)
	{
		final MinecraftClient client = MinecraftClient.getInstance();
		
		final float healthScale = ScaleUtils.getHealthScale(client.getCameraEntity(), client.getTickDelta());
		
		return healthScale != 1.0F ? value * healthScale : value;
	}
}
