package virtuoel.pehkui.mixin.client.compat114;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.client.MinecraftClient;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(targets = MixinConstants.HELD_ITEM_RENDERER, remap = false)
public abstract class HeldItemRendererMixin
{
	@Shadow(remap = false)
	MinecraftClient field_4050; // UNMAPPED_FIELD
	
	@ModifyConstant(method = MixinConstants.RENDER_OVERLAYS, constant = @Constant(floatValue = 0.1F))
	private float renderOverlaysModifyOffset(float value)
	{
		final float scale = ScaleUtils.getHeightScale(field_4050.player);
		
		return scale != 1.0F ? value * scale : value;
	}
}
