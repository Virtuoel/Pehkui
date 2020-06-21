package virtuoel.pehkui.mixin.compat115minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.Entity;
import virtuoel.pehkui.api.ScaleData;

@Mixin(Entity.class)
public abstract class EntityMixin
{
	@ModifyConstant(method = "isInsideWall()Z", constant = @Constant(floatValue = 0.1F))
	private float isInsideWallModifyOffset(float value)
	{
		final float scale = ScaleData.of((Entity) (Object) this).getScale();
		
		return scale != 1.0F ? value * scale : value;
	}
}
