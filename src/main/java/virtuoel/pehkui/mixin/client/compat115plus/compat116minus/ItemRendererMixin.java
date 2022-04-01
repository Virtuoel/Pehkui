package virtuoel.pehkui.mixin.client.compat115plus.compat116minus;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(value = ItemRenderer.class, priority = 1010)
public class ItemRendererMixin
{
	@Inject(method = "method_23177(Lnet/minecraft/class_1309;Lnet/minecraft/class_1799;Lnet/minecraft/class_809$class_811;ZLnet/minecraft/class_4587;Lnet/minecraft/class_4597;Lnet/minecraft/class_1937;II)V", at = @At(value = "HEAD"), remap = false)
	private void onRenderItemPreRender(@Nullable LivingEntity entity, ItemStack item, ModelTransformation.Mode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, @Nullable World world, int light, int overlay, CallbackInfo info)
	{
		matrices.push();
		
		if (!item.isEmpty() && entity != null)
		{
			final float tickDelta = MinecraftClient.getInstance().getTickDelta();
			final float scale = ScaleUtils.getHeldItemScale(entity, tickDelta);
			
			if (scale != 1.0F)
			{
				matrices.scale(scale, scale, scale);
			}
		}
		
		matrices.push();
	}
	
	@Inject(method = "method_23177(Lnet/minecraft/class_1309;Lnet/minecraft/class_1799;Lnet/minecraft/class_809$class_811;ZLnet/minecraft/class_4587;Lnet/minecraft/class_4597;Lnet/minecraft/class_1937;II)V", at = @At(value = "RETURN"), remap = false)
	private void onRenderItemPostRender(@Nullable LivingEntity entity, ItemStack item, ModelTransformation.Mode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, @Nullable World world, int light, int overlay, CallbackInfo info)
	{
		matrices.pop();
		matrices.pop();
	}
}
