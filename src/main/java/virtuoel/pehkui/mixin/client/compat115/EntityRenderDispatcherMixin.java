package virtuoel.pehkui.mixin.client.compat115;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import virtuoel.pehkui.util.MixinConstants;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin
{
	@ModifyConstant(target = @Desc(value = MixinConstants.RENDER_SHADOW_PART, args = { MatrixStack.Entry.class, VertexConsumer.class, WorldView.class, BlockPos.class, double.class, double.class, double.class, float.class, float.class }), constant = @Constant(doubleValue = 0.015625D), remap = false)
	private static double renderShadowPartModifyShadowHeight(double value)
	{
		return value - 0.0155D;
	}
}
