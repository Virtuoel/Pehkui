package virtuoel.pehkui.mixin.compat116plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.Entity;
import virtuoel.pehkui.entity.ResizableEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(Entity.class)
public abstract class EntityMixin implements ResizableEntity
{
	@ModifyConstant(method = "isInsideWall()Z", constant = @Constant(doubleValue = 0.10000000149011612D))
	private double isInsideWallModifyOffset(double value)
	{
		final float scale = ScaleUtils.getHeightScale(this);
		
		return scale != 1.0F ? value * scale : value;
	}
	
	@ModifyConstant(method = "updateSubmergedInWaterState()V", constant = @Constant(doubleValue = 0.1111111119389534D))
	private double iupdateSubmergedInWaterStateModifyOffset(double value)
	{
		final float scale = ScaleUtils.getHeightScale(this);
		
		return scale != 1.0F ? value * scale : value;
	}
}
