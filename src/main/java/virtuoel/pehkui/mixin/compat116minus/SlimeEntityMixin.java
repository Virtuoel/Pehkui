package virtuoel.pehkui.mixin.compat116minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.world.World;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(SlimeEntity.class)
public class SlimeEntityMixin
{
	@ModifyArg(target = @Desc(value = MixinConstants.REMOVE), at = @At(value = "INVOKE", desc = @Desc(owner = World.class, value = MixinConstants.SPAWN_ENTITY, args = { Entity.class }, ret = boolean.class), remap = false), remap = false)
	private Entity removeSpawnEntityProxy(Entity entity)
	{
		ScaleUtils.loadScale(entity, (Entity) (Object) this);
		
		return entity;
	}
	
	@ModifyConstant(target = @Desc(value = MixinConstants.REMOVE), constant = @Constant(floatValue = 4.0F), remap = false)
	private float removeModifyHorizontalOffset(float value)
	{
		final float scale = ScaleUtils.getBoundingBoxWidthScale((Entity) (Object) this);
		
		if (scale != 1.0F)
		{
			return value / scale;
		}
		
		return value;
	}
	
	@ModifyConstant(target = @Desc(value = MixinConstants.REMOVE), constant = @Constant(doubleValue = 0.5D), remap = false)
	private double removeModifyVerticalOffset(double value)
	{
		final float scale = ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this);
		
		if (scale != 1.0F)
		{
			return value * scale;
		}
		
		return value;
	}
}
