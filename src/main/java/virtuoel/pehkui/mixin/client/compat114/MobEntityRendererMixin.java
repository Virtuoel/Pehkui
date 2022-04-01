package virtuoel.pehkui.mixin.client.compat114;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(MobEntityRenderer.class)
public class MobEntityRendererMixin<T extends MobEntity>
{
	@Inject(method = "method_4073", at = @At(value = "HEAD"), remap = false)
	private void onRenderLeashPreRender(T entity, double x, double y, double z, float yaw, float tickDelta, CallbackInfo info)
	{
		final Entity attached = entity.getHoldingEntity();
		
		if (attached != null)
		{
			final float inverseWidthScale = 1.0F / ScaleUtils.getModelWidthScale(entity, tickDelta);
			final float inverseHeightScale = 1.0F / ScaleUtils.getModelHeightScale(entity, tickDelta);
			
			GL11.glPushMatrix();
			GL11.glScalef(inverseWidthScale, inverseHeightScale, inverseWidthScale);
			GL11.glPushMatrix();
		}
	}
	
	@Inject(method = "method_4073", at = @At(value = "RETURN"), remap = false)
	private void onRenderLeashPostRender(T entity, double x, double y, double z, float yaw, float tickDelta, CallbackInfo info)
	{
		if (entity.getHoldingEntity() != null)
		{
			GL11.glPopMatrix();
			GL11.glPopMatrix();
		}
	}
}
