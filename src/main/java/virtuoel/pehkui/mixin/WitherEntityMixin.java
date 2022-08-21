package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.WitherEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(WitherEntity.class)
public class WitherEntityMixin
{
	@ModifyConstant(method = "getHeadX", constant = @Constant(doubleValue = 1.3))
	private double pehkui$getHeadX$offset(double value)
	{
		final float scale = ScaleUtils.getBoundingBoxWidthScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
	
	@ModifyConstant(method = "getHeadZ", constant = @Constant(doubleValue = 1.3))
	private double pehkui$getHeadZ$offset(double value)
	{
		final float scale = ScaleUtils.getBoundingBoxWidthScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
	
	@ModifyConstant(method = "getHeadY", constant = { @Constant(doubleValue = 3.0), @Constant(doubleValue = 2.2) })
	private double pehkui$getHeadY$offset(double value)
	{
		final float scale = ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
}
