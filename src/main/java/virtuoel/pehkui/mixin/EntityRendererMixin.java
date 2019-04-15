package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import virtuoel.pehkui.api.ResizableEntity;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin
{
	@Shadow float field_4673;
	@Shadow abstract void renderShadow(Entity entity_1, double double_1, double double_2, double double_3, float float_1, float float_2);
	
	@Redirect(method = "renderLabel(Lnet/minecraft/entity/Entity;Ljava/lang/String;DDDI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getHeight()F"))
	public float onRenderLabelGetHeightProxy(Entity entity)
	{
		return (entity.getHeight() / ((ResizableEntity) entity).getScale());
	}
	
	@Redirect(method = "postRender(Lnet/minecraft/entity/Entity;DDDFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderer;renderShadow(Lnet/minecraft/entity/Entity;DDDFF)V"))
	public void onPostRenderRenderShadowProxy(EntityRenderer<Entity> obj, Entity entity, double x, double y, double z, float float_1, float float_2)
	{
		final float shadowSize_old = field_4673;
		final float scale = MathHelper.lerp(float_2, ((ResizableEntity) entity).getPrevScale(), ((ResizableEntity) entity).getScale());
		
		field_4673 *= scale;
		
		GlStateManager.pushMatrix();
		GlStateManager.translated(0, -0.0155, 0);
		GlStateManager.pushMatrix();
		
		renderShadow(entity, x, y, z, float_1, float_2);
		
		GlStateManager.popMatrix();
		GlStateManager.popMatrix();
		
		field_4673 = shadowSize_old;
	}
}
