package virtuoel.pehkui.mixin.client.compat116minus;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin
{
	@Shadow
	abstract PlayerEntity getCameraPlayer();
	
	@Shadow @Final @Mutable
	MinecraftClient client;
	
	@ModifyVariable(method = "renderStatusBars", at = @At(value = "INVOKE", shift = Shift.BEFORE, target = "Lnet/minecraft/entity/player/PlayerEntity;getAbsorptionAmount()F"))
	private float onRenderStatusBars(float value)
	{
		final float healthScale = ScaleUtils.getHealthScale(getCameraPlayer(), client.getTickDelta());
		
		return healthScale != 1.0F ? value * healthScale : value;
	}
}
