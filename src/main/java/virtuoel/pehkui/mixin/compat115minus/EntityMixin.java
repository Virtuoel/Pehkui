package virtuoel.pehkui.mixin.compat115minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.Entity;
import virtuoel.pehkui.entity.ResizableEntity;

@Mixin(Entity.class)
public abstract class EntityMixin implements ResizableEntity
{
	@ModifyConstant(method = "isInsideWall()Z", constant = @Constant(floatValue = 0.1F))
	private float isInsideWallModifyOffset(float value)
	{
		final float scale = pehkui_getScaleData().getScale();
		
		return scale != 1.0F ? value * scale : value;
	}
}
