package virtuoel.pehkui.mixin.compat114;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.mixin.EntityMixin;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin
{
	@ModifyArg(method = "method_6078", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1937;method_8649(Lnet/minecraft/class_1297;)Z", remap = false), remap = false)
	private Entity onDeathModifyEntity(Entity entity)
	{
		final float scale = pehkui_scaleData.getScale();
		
		if (scale != 1.0F)
		{
			final ScaleData data = ScaleData.of(entity);
			data.setScale(scale);
			data.setTargetScale(scale);
			data.markForSync();
		}
		
		return entity;
	}
	
	@ModifyArg(method = "method_6108", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1937;method_8649(Lnet/minecraft/class_1297;)Z", remap = false), remap = false)
	private Entity updatePostDeathModifyEntity(Entity entity)
	{
		final float scale = pehkui_scaleData.getScale();
		
		if (scale != 1.0F)
		{
			final ScaleData data = ScaleData.of(entity);
			data.setScale(scale);
			data.setTargetScale(scale);
			data.markForSync();
		}
		
		return entity;
	}
}
