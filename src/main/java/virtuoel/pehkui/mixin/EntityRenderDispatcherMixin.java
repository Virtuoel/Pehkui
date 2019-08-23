package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import virtuoel.pehkui.api.ScaleData;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin
{
	@Redirect(method = "render(Lnet/minecraft/entity/Entity;DDDFFZ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderer;render(Lnet/minecraft/entity/Entity;DDDFF)V"))
	public void onRenderEntityRendererRenderProxy(EntityRenderer<Entity> obj, Entity entity, double x, double y, double z, float float_1, float float_2)
	{
		final float scale = MathHelper.lerp(float_2, ScaleData.of(entity).getPrevScale(), ScaleData.of(entity).getScale());
		
		GlStateManager.pushMatrix();
		GlStateManager.scalef(scale, scale, scale);
		GlStateManager.translated((x / scale) - x, (y / scale) - y, (z / scale) - z);
		GlStateManager.pushMatrix();
		
		obj.render(entity, x, y, z, float_1, float_2);
		
		GlStateManager.popMatrix();
		GlStateManager.popMatrix();
	}
}
