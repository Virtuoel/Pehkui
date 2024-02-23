package virtuoel.pehkui.mixin.client.compat115plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.entity.player.PlayerEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(InGameOverlayRenderer.class)
public abstract class InGameOverlayRendererMixin
{
	@ModifyExpressionValue(method = "getInWallBlockState", at = @At(value = "CONSTANT", args = "floatValue=0.1F"))
	private static float pehkui$getInWallBlockState$offset(float value, PlayerEntity player)
	{
		final float scale = ScaleUtils.getEyeHeightScale(player);
		
		return scale != 1.0F ? value * scale : value;
	}
}
