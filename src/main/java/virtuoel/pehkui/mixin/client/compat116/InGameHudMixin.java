package virtuoel.pehkui.mixin.client.compat116;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin
{
	@ModifyVariable(target = @Desc(value = MixinConstants.RENDER_STATUS_BARS, args = { MatrixStack.class }), at = @At(value = "INVOKE", shift = Shift.BEFORE, desc = @Desc(owner = PlayerEntity.class, value = MixinConstants.GET_ABSORPTION_AMOUNT, ret = float.class), remap = false), remap = false)
	private float onRenderStatusBars(float value)
	{
		final MinecraftClient client = MinecraftClient.getInstance();
		
		final float healthScale = ScaleUtils.getHealthScale(client.getCameraEntity(), client.getTickDelta());
		
		return healthScale != 1.0F ? value * healthScale : value;
	}
}
