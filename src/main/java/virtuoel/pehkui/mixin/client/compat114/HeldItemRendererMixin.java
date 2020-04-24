package virtuoel.pehkui.mixin.client.compat114;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.client.MinecraftClient;
import virtuoel.pehkui.api.ScaleData;

@Mixin(targets = "net.minecraft.class_759", remap = false)
public abstract class HeldItemRendererMixin
{
	@Shadow(remap = false)
	MinecraftClient field_4050;
	
	@ModifyConstant(method = "method_3232(F)V", constant = @Constant(floatValue = 0.1F))
	private float renderOverlaysModifyOffset(float value)
	{
		final float scale = ScaleData.of(field_4050.player).getScale();
		
		return scale != 1.0F ? value * scale : value;
	}
}
