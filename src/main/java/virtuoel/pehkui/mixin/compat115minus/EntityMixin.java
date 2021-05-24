package virtuoel.pehkui.mixin.compat115minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluid;
import net.minecraft.tag.Tag;
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
		final float scale = ScaleUtils.getHeightScale((Entity) (Object) this);
		
		return scale != 1.0F ? value * scale : value;
	}
	
	@Inject(at = @At("HEAD"), method = "method_5692(Lnet/minecraft/class_3494;)Z", cancellable = true, remap = false)
	private void onUpdateMovementInFluid(Tag<Fluid> tag, CallbackInfoReturnable<Boolean> info)
	{
		if (ScaleUtils.isAboveCollisionThreshold((Entity) (Object) this))
		{
			info.setReturnValue(false);
		}
	}
}
