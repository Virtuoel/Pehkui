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
	@ModifyConstant(method = "getInWallBlockState", constant = @Constant(floatValue = 0.1F))
	private static float getInWallBlockStateModifyOffset(float value, PlayerEntity player)
	{
		final float scale = ScaleUtils.getHeightScale(player);
		
		return scale != 1.0F ? value * scale : value;
	}
}
