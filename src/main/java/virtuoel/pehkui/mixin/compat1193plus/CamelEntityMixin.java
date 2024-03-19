package virtuoel.pehkui.mixin.compat1193plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.passive.CamelEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(CamelEntity.class)
public class CamelEntityMixin
{
	@ModifyReturnValue(method = "getDimensions", at = @At("RETURN"))
	private EntityDimensions pehkui$getDimensions(EntityDimensions original, EntityPose pose)
	{
		if (pose == EntityPose.SITTING)
		{
			original = original.scaled(ScaleUtils.getBoundingBoxWidthScale((Entity) (Object) this), ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this));
		}
		
		return original;
	}
}
