package virtuoel.pehkui.mixin.client.compat114;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import virtuoel.pehkui.Pehkui;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(value = ItemRenderer.class, priority = 1010)
public class ItemRendererMixin
{
	@Unique
	private static ItemStack lastRenderedStack = null;
	@Unique
	private static ItemStack lastLoggedStack = null;
	
	@Inject(method = MixinConstants.RENDER_HELD_ITEM, at = @At(value = "HEAD"), remap = false)
	private void onRenderHeldItemPreRender(ItemStack stack, LivingEntity entity, @Coerce Object type, boolean leftHanded, CallbackInfo info)
	{
		if (lastRenderedStack != null && lastRenderedStack != lastLoggedStack)
		{
			final String stackKey = lastRenderedStack.getTranslationKey();
			final String itemKey = lastRenderedStack.getItem().getTranslationKey();
			if (stackKey.equals(itemKey))
			{
				Pehkui.LOGGER.fatal("[{}]: Was item rendering cancelled? Matrix stack not popped after rendering item {} ({}).", Pehkui.MOD_ID, stackKey, lastRenderedStack.getItem());
			}
			else
			{
				Pehkui.LOGGER.fatal("[{}]: Was item rendering cancelled? Matrix stack not popped after rendering item {} ({}) ({})", Pehkui.MOD_ID, stackKey, itemKey, lastRenderedStack.getItem());
			}
			
			lastLoggedStack = lastRenderedStack;
		}
		
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
		
		lastRenderedStack = stack;
	}
	
	@Inject(method = MixinConstants.RENDER_HELD_ITEM, at = @At(value = "RETURN"), remap = false)
	private void onRenderHeldItemPostRender(ItemStack stack, LivingEntity entity, @Coerce Object type, boolean leftHanded, CallbackInfo info)
	{
		lastRenderedStack = null;
		lastLoggedStack = null;
		
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}
}
