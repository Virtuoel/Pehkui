package virtuoel.pehkui.mixin.client.compat115;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin
{
	@Redirect(method = MixinConstants.RENDER_LABEL_IF_PRESENT, at = @At(value = "INVOKE", target = MixinConstants.GET_HEIGHT, remap = false), remap = false)
	private float pehkui$renderLabelIfPresent$getHeight(Entity entity)
	{
		final float delta = MinecraftClient.getInstance().getTickDelta();
		return entity.getHeight() / ScaleUtils.getBoundingBoxHeightScale(entity, delta);
	}
}
