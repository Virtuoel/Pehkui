package virtuoel.pehkui.mixin.client.compat115minus;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin
{
	@Shadow(remap = false)
	abstract PlayerEntity method_1737(); // UNMAPPED_METHOD
	
	@Shadow(remap = false) @Final @Mutable
	MinecraftClient field_2035; // UNMAPPED_FIELD
	
	@ModifyArg(method = MixinConstants.RENDER_STATUS_BARS, index = 0, at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(FF)F"))
	private float onRenderStatusBars(float value)
	{
		final float healthScale = ScaleUtils.getHealthScale(method_1737(), field_2035.getTickDelta());
		
		return healthScale != 1.0F ? value * healthScale : value;
	}
}
