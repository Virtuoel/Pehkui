package virtuoel.pehkui.mixin;

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
	@ModifyArg(method = "remove()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
	private Entity removeSpawnEntityProxy(Entity entity)
	{
		ScaleUtils.loadScale(entity, this, true);
		
		return entity;
	}
	
	@ModifyConstant(method = "remove()V", constant = @Constant(floatValue = 0.5F))
	private float removeModifyHorizontalOffset(float value)
	{
		final float scale = ScaleUtils.getWidthScale(this);
		
		if (scale != 1.0F)
		{
			return value * scale;
		}
		
		return value;
	}
	
	@ModifyConstant(method = "remove()V", constant = @Constant(doubleValue = 0.5D))
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
