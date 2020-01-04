package virtuoel.pehkui.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import virtuoel.pehkui.api.ScaleData;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin
{
	@Redirect(method = "renderLabelIfPresent(Lnet/minecraft/entity/Entity;Ljava/lang/String;DDDI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getHeight()F"))
	public float renderLabelIfPresentGetHeightProxy(Entity entity)
	{
		return (entity.getHeight() / ScaleData.of(entity).getScale());
	}
}
