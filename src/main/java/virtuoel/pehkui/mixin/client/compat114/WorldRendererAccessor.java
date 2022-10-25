package virtuoel.pehkui.mixin.client.compat114;

import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import virtuoel.pehkui.util.MixinConstants;

@Mixin(targets = MixinConstants.WORLD_RENDERER)
public interface WorldRendererAccessor
{
	@Invoker(value = "method_3260", remap = false)
	public static void pehkui$drawBoxOutline(Box box, float red, float green, float blue, float alpha)
	{
		throw new NoSuchMethodError();
	}
}
