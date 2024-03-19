package virtuoel.pehkui.mixin.client.compat114;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin
{
	@Dynamic
	@Shadow
	float field_4673; // UNMAPPED_FIELD
	
	@Dynamic
	@WrapOperation(method = MixinConstants.RENDER_LABEL, at = @At(value = "INVOKE", target = MixinConstants.GET_HEIGHT))
	private float pehkui$renderLabel$getHeight(Entity entity, Operation<Float> original)
	{
		final float delta = MinecraftClient.getInstance().getTickDelta();
		return original.call(entity) / ScaleUtils.getBoundingBoxHeightScale(entity, delta);
	}
	
	@Dynamic
	@WrapOperation(method = MixinConstants.POST_RENDER, at = @At(value = "INVOKE", target = MixinConstants.RENDER_SHADOW))
	private void pehkui$postRender$renderShadow(EntityRenderer<Entity> obj, Entity entity, double x, double y, double z, float opacity, float tickDelta, Operation<Void> original)
	{
		final float scale = ScaleUtils.getModelWidthScale(entity, tickDelta);
		
		if (scale != 1.0F)
		{
			final float temp = field_4673;
			
			field_4673 *= scale;
			
			GL11.glPushMatrix();
			GL11.glTranslated(0, -0.0155, 0);
			GL11.glPushMatrix();
			
			original.call(obj, entity, x, y, z, opacity, tickDelta);
			
			GL11.glPopMatrix();
			GL11.glPopMatrix();
			
			field_4673 = temp;
		}
		else
		{
			original.call(obj, entity, x, y, z, opacity, tickDelta);
		}
	}
}
