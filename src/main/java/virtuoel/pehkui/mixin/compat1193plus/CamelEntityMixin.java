package virtuoel.pehkui.mixin.compat1193plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
// import net.minecraft.entity.passive.CamelEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(/*Camel*/Entity.class)
public class CamelEntityMixin
{
	// TODO 1.19.3
	@Inject(at = @At("RETURN"), method = "getDimensions", cancellable = true)
	private void pehkui$getDimensions(EntityPose pose, CallbackInfoReturnable<EntityDimensions> info)
	{
	//	if (pose == EntityPose.SITTING)
		{
			info.setReturnValue(info.getReturnValue().scaled(ScaleUtils.getBoundingBoxWidthScale((Entity) (Object) this), ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this)));
		}
	}
}
