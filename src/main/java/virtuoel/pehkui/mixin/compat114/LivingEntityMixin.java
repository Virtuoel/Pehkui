package virtuoel.pehkui.mixin.compat114;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import virtuoel.pehkui.mixin.EntityMixin;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin
{
	@ModifyArg(method = "method_6078", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1937;method_8649(Lnet/minecraft/class_1297;)Z", remap = false), remap = false)
	private Entity onDeathModifyEntity(Entity entity)
	{
		ScaleUtils.setScale(entity, ScaleUtils.getDropScale(this));
		
		return entity;
	}
	
	@ModifyArg(method = "method_6108", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1937;method_8649(Lnet/minecraft/class_1297;)Z", remap = false), remap = false)
	private Entity updatePostDeathModifyEntity(Entity entity)
	{
		ScaleUtils.setScale(entity, ScaleUtils.getDropScale(this));
		
		return entity;
	}
}
