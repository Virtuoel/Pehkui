package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ItemEntity.class)
public class ItemEntityMixin
{
	@ModifyConstant(method = "tryMerge", constant = @Constant(doubleValue = 0.5D))
	private double tryMergeModifyWidth(double value)
	{
		final float scale = ScaleUtils.getWidthScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
}
