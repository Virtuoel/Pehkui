package virtuoel.pehkui.mixin.client.compat114;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleRenderUtils;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(value = ItemRenderer.class, priority = 1010)
public class ItemRendererMixin
{
	@Dynamic
	@Inject(method = MixinConstants.RENDER_HELD_ITEM, at = @At(value = "HEAD"))
	private void pehkui$renderHeldItem$head(ItemStack stack, LivingEntity entity, @Coerce Object type, boolean leftHanded, CallbackInfo info)
	{
		if (ScaleRenderUtils.shouldSkipHeadItemScaling(entity, stack, type))
		{
			return;
		}
		
		ScaleRenderUtils.logIfItemRenderCancelled();
		
		GL11.glPushMatrix();
		
		if (!stack.isEmpty() && entity != null)
		{
			final float tickDelta = MinecraftClient.getInstance().getTickDelta();
			final float scale = ScaleUtils.getHeldItemScale(entity, tickDelta);
			
			if (scale != 1.0F)
			{
				GL11.glScalef(scale, scale, scale);
			}
		}
		
		GL11.glPushMatrix();
		
		ScaleRenderUtils.saveLastRenderedItem(stack);
	}
	
	@Dynamic
	@Inject(method = MixinConstants.RENDER_HELD_ITEM, at = @At(value = "RETURN"))
	private void pehkui$renderHeldItem$return(ItemStack stack, LivingEntity entity, @Coerce Object type, boolean leftHanded, CallbackInfo info)
	{
		if (ScaleRenderUtils.shouldSkipHeadItemScaling(entity, stack, type))
		{
			return;
		}
		
		ScaleRenderUtils.clearLastRenderedItem();
		
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}
}
