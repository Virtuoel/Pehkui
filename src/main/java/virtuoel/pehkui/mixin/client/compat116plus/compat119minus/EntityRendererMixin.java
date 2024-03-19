package virtuoel.pehkui.mixin.client.compat116plus.compat119minus;

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
	@WrapOperation(method = "renderLabelIfPresent(Lnet/minecraft/entity/Entity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getHeight()F"))
	private float pehkui$renderLabelIfPresent$getHeight(Entity entity, Operation<Float> original)
	{
		final float delta = MinecraftClient.getInstance().getTickDelta();
		return original.call(entity) / ScaleUtils.getBoundingBoxHeightScale(entity, delta);
	}
}
