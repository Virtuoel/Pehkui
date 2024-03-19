package virtuoel.pehkui.mixin.compat117plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.SlimeEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(SlimeEntity.class)
public class SlimeEntityMixin
{
	@ModifyArg(method = "remove(Lnet/minecraft/entity/Entity$RemovalReason;)V", at = @At(value = "INVOKE", target = "Ljava/util/ArrayList;add(Ljava/lang/Object;)Z"))
	private Object pehkui$remove$spawnEntity(Object entity)
	{
		ScaleUtils.loadScale((Entity) entity, (Entity) (Object) this);
		
		return entity;
	}
	
	@ModifyExpressionValue(method = "remove(Lnet/minecraft/entity/Entity$RemovalReason;)V", at = @At(value = "CONSTANT", args = "floatValue=4.0F"))
	private float pehkui$remove$horizontalOffset(float value)
	{
		final float scale = ScaleUtils.getBoundingBoxWidthScale((Entity) (Object) this);
		
		if (scale != 1.0F)
		{
			return value / scale;
		}
		
		return value;
	}
	
	@ModifyExpressionValue(method = "remove(Lnet/minecraft/entity/Entity$RemovalReason;)V", at = @At(value = "CONSTANT", args = "doubleValue=0.5D"))
	private double pehkui$remove$verticalOffset(double value)
	{
		final float scale = ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this);
		
		if (scale != 1.0F)
		{
			return value * scale;
		}
		
		return value;
	}
}
