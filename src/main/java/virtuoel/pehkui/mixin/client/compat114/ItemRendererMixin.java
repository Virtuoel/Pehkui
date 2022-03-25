package virtuoel.pehkui.mixin.client.compat114;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(value = ItemRenderer.class, priority = 1010)
public class ItemRendererMixin
{
	@Inject(target = @Desc(value = MixinConstants.RENDER_HELD_ITEM, args = { ItemStack.class, LivingEntity.class, ModelTransformation.Mode.class, boolean.class }), at = @At(value = "HEAD"), remap = false)
	private void onRenderHeldItemPreRender(ItemStack stack, LivingEntity entity, ModelTransformation.Mode type, boolean leftHanded, CallbackInfo info)
	{
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
	}
	
	@Inject(target = @Desc(value = MixinConstants.RENDER_HELD_ITEM, args = { ItemStack.class, LivingEntity.class, ModelTransformation.Mode.class, boolean.class }), at = @At(value = "RETURN"), remap = false)
	private void onRenderHeldItemPostRender(ItemStack stack, LivingEntity entity, ModelTransformation.Mode type, boolean leftHanded, CallbackInfo info)
	{
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}
}
