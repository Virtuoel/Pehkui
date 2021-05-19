package virtuoel.pehkui.mixin.compat116plus;

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
import net.minecraft.util.math.BlockPos;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(Entity.class)
public abstract class EntityMixin
{
	@Shadow
	private BlockPos blockPos;
	
	@Unique
	protected void setPosDirectly(final BlockPos pos)
	{
		blockPos = pos;
	}
	
	@ModifyConstant(method = "updateSubmergedInWaterState()V", constant = @Constant(doubleValue = 0.1111111119389534D))
	private double updateSubmergedInWaterStateModifyOffset(double value)
	{
		final float scale = ScaleUtils.getHeightScale((Entity) (Object) this);
		
		return scale != 1.0F ? value * scale : value;
	}
	
	@Inject(at = @At("HEAD"), method = "updateMovementInFluid(Lnet/minecraft/tag/Tag;D)Z", cancellable = true)
	private void onUpdateMovementInFluid(Tag<Fluid> tag, double d, CallbackInfoReturnable<Boolean> info)
	{
		if (ScaleUtils.isAboveCollisionThreshold((Entity) (Object) this))
		{
			info.setReturnValue(false);
		}
	}
}
