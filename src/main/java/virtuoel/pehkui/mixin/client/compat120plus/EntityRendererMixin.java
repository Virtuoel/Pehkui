package virtuoel.pehkui.mixin.client.compat120plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin
{
	@WrapOperation(method = "renderLabelIfPresent(Lnet/minecraft/entity/Entity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getNameLabelHeight()F"))
	private float pehkui$renderLabelIfPresent$getNameLabelHeight(Entity entity, Operation<Float> original)
	{
		final float delta = MinecraftClient.getInstance().getTickDelta();
		return (original.call(entity) - entity.getHeight()) + (entity.getHeight() / ScaleUtils.getBoundingBoxHeightScale(entity, delta));
	}
}
