package virtuoel.pehkui.mixin.compat116minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.SlimeEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(SlimeEntity.class)
public class SlimeEntityMixin
{
	@ModifyArg(method = "method_5650", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1937;method_8649(Lnet/minecraft/class_1297;)Z", remap = false), remap = false)
	private Entity removeSpawnEntityProxy(Entity entity)
	{
		ScaleUtils.loadScale(entity, this);
		
		return entity;
	}
	
	@ModifyConstant(method = "method_5650", constant = @Constant(floatValue = 4.0F), remap = false)
	private float removeModifyHorizontalOffset(float value)
	{
		final float scale = ScaleUtils.getWidthScale(this);
		
		if (scale != 1.0F)
		{
			return value / scale;
		}
		
		return value;
	}
	
	@ModifyConstant(method = "method_5650", constant = @Constant(doubleValue = 0.5D), remap = false)
	private double removeModifyVerticalOffset(double value)
	{
		final float scale = ScaleUtils.getHeightScale(this);
		
		if (scale != 1.0F)
		{
			return value * scale;
		}
		
		return value;
	}
}
