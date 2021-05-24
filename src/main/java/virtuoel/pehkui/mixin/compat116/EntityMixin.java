package virtuoel.pehkui.mixin.compat116;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.Entity;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(Entity.class)
public abstract class EntityMixin
{
	@ModifyConstant(method = MixinConstants.IS_INSIDE_WALL, constant = @Constant(doubleValue = 0.10000000149011612D), remap = false)
	private double isInsideWallModifyOffset(double value)
	{
		final float scale = ScaleUtils.getHeightScale((Entity) (Object) this);
		
		return scale != 1.0F ? value * scale : value;
	}
}
