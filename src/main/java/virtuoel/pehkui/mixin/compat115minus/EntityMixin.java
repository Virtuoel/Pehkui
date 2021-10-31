package virtuoel.pehkui.mixin.compat115minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(Entity.class)
public abstract class EntityMixin
{
	@Shadow(remap = false)
	private double field_5987; // UNMAPPED_FIELD
	@Shadow(remap = false)
	private double field_6010; // UNMAPPED_FIELD
	@Shadow(remap = false)
	private double field_6035; // UNMAPPED_FIELD
	@Shadow(remap = false)
	abstract Vec3d method_5812(); // UNMAPPED_METHOD
	
	@Unique
	protected void setPosDirectly(final double x, final double y, final double z)
	{
		field_5987 = x;
		field_6010 = y;
		field_6035 = z;
	}
	
	@ModifyConstant(method = "isInsideWall()Z", constant = @Constant(floatValue = 0.1F))
	private float isInsideWallModifyOffset(float value)
	{
		final float scale = ScaleUtils.getEyeHeightScale((Entity) (Object) this);
		
		return scale != 1.0F ? value * scale : value;
	}
}
