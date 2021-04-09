package virtuoel.pehkui.mixin.client.compat114;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin
{
	@Shadow(remap = false)
	float field_4673; // UNMAPPED_FIELD
	
	@Shadow(remap = false)
	abstract void method_3934(Entity entity, double x, double y, double z, float opacity, float tickDelta); // UNMAPPED_METHOD
	
	@Redirect(method = MixinConstants.RENDER_LABEL, at = @At(value = "INVOKE", target = MixinConstants.GET_HEIGHT, remap = false), remap = false)
	private float renderLabelGetHeightProxy(Entity entity)
	{
		return entity.getHeight() / ScaleUtils.getHeightScale(entity);
	}
	
	@Redirect(method = MixinConstants.POST_RENDER, at = @At(value = "INVOKE", target = MixinConstants.RENDER_SHADOW, remap = false), remap = false)
	private void onPostRenderRenderShadowProxy(EntityRenderer<Entity> obj, Entity entity, double x, double y, double z, float opacity, float tickDelta)
	{
		final float scale = ScaleUtils.getWidthScale(entity, tickDelta);
		
		if (scale != 1.0F)
		{
			final float temp = field_4673;
			
			field_4673 *= scale;
			
			GL11.glPushMatrix();
			GL11.glTranslated(0, -0.0155, 0);
			GL11.glPushMatrix();
			
			method_3934(entity, x, y, z, opacity, tickDelta);
			
			GL11.glPopMatrix();
			GL11.glPopMatrix();
			
			field_4673 = temp;
		}
		else
		{
			method_3934(entity, x, y, z, opacity, tickDelta);
		}
	}
}
