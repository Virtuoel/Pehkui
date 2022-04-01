package virtuoel.pehkui.mixin.client.compat114;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(value = ItemRenderer.class, priority = 1010)
public class ItemRendererMixin
{
	@Inject(method = "method_4016(Lnet/minecraft/class_1799;Lnet/minecraft/class_1309;Lnet/minecraft/class_809$class_811;Z)V", at = @At(value = "HEAD"), remap = false)
	private void onRenderHeldItemPreRender(ItemStack stack, LivingEntity entity, @Coerce Object type, boolean leftHanded, CallbackInfo info)
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
	
	@Inject(method = "method_4016(Lnet/minecraft/class_1799;Lnet/minecraft/class_1309;Lnet/minecraft/class_809$class_811;Z)V", at = @At(value = "RETURN"), remap = false)
	private void onRenderHeldItemPostRender(ItemStack stack, LivingEntity entity, @Coerce Object type, boolean leftHanded, CallbackInfo info)
	{
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}
}
