package virtuoel.pehkui.mixin.client.compat115;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin
{
	@Redirect(method = "method_3926(Lnet/minecraft/class_1297;Ljava/lang/String;Lnet/minecraft/class_4587;Lnet/minecraft/class_4597;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1297;method_17682()F", remap = false), remap = false)
	private float renderLabelIfPresentGetHeightProxy(Entity entity)
	{
		return entity.getHeight() / ScaleUtils.getHeightScale(entity);
	}
}
