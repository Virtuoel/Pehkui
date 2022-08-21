package virtuoel.pehkui.mixin.client.compat115plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(MobEntityRenderer.class)
public class MobEntityRendererMixin<T extends MobEntity>
{
	@Inject(method = "method_4073", at = @At(value = "HEAD"))
	private <E extends Entity> void pehkui$renderLeash$head(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider provider, E holdingEntity, CallbackInfo info)
	{
		final Entity attached = entity.getHoldingEntity();
		
		if (attached != null)
		{
			final float inverseWidthScale = 1.0F / ScaleUtils.getModelWidthScale(entity, tickDelta);
			final float inverseHeightScale = 1.0F / ScaleUtils.getModelHeightScale(entity, tickDelta);
			
			matrices.push();
			matrices.scale(inverseWidthScale, inverseHeightScale, inverseWidthScale);
			matrices.push();
		}
	}
	
	@Inject(method = "method_4073", at = @At(value = "RETURN"))
	private <E extends Entity> void pehkui$renderLeash$return(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider provider, E holdingEntity, CallbackInfo info)
	{
		if (entity.getHoldingEntity() != null)
		{
			matrices.pop();
			matrices.pop();
		}
	}
}
