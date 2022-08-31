package virtuoel.pehkui.mixin.client.compat115plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.entity.player.PlayerEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(InGameOverlayRenderer.class)
public abstract class InGameOverlayRendererMixin
{
	@ModifyConstant(method = "getOverlayBlock", constant = @Constant(floatValue = 0.1F), remap = false)
	private static float pehkui$getInWallBlockState$offset(float value, PlayerEntity player)
	{
		final float scale = ScaleUtils.getEyeHeightScale(player);
		
		return scale != 1.0F ? value * scale : value;
	}
}
