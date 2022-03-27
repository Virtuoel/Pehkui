package virtuoel.pehkui.mixin.client.compat115;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin
{
	@Redirect(target = @Desc(value = MixinConstants.RENDER_LABEL_IF_PRESENT, args = { Entity.class, String.class, MatrixStack.class, VertexConsumerProvider.class, int.class }), at = @At(value = "INVOKE", desc = @Desc(owner = Entity.class, value = MixinConstants.GET_HEIGHT, ret = float.class), remap = false), remap = false)
	private float renderLabelIfPresentGetHeightProxy(Entity entity)
	{
		return entity.getHeight() / ScaleUtils.getModelHeightScale(entity);
	}
}
