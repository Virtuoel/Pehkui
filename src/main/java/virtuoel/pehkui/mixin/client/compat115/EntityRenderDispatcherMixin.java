package virtuoel.pehkui.mixin.client.compat115;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.client.render.entity.EntityRenderDispatcher;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin
{
	@ModifyConstant(method = "method_23163", constant = @Constant(doubleValue = 0.015625D), remap = false)
	private static double renderShadowPartModifyShadowHeight(double value)
	{
		return value - 0.0155D;
	}
}
