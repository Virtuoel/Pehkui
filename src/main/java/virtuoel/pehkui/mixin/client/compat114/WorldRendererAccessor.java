package virtuoel.pehkui.mixin.client.compat114;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.math.Box;

@Mixin(WorldRenderer.class)
public interface WorldRendererAccessor
{
	@Invoker(value = "method_3260", remap = false)
	public static void pehkui$drawBoxOutline(Box box, float red, float green, float blue, float alpha)
	{
		throw new NoSuchMethodError();
	}
}
