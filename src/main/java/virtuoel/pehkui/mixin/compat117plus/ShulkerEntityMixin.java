package virtuoel.pehkui.mixin.compat117plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ShulkerEntity.class)
public class ShulkerEntityMixin
{
	@ModifyReturnValue(method = "calculateBoundingBox", at = @At("RETURN"))
	private Box pehkui$calculateBoundingBox(Box box)
	{
		final ShulkerEntity entity = (ShulkerEntity) (Object) this;
		
		final float widthScale = ScaleUtils.getBoundingBoxWidthScale(entity);
		final float heightScale = ScaleUtils.getBoundingBoxHeightScale(entity);
		
		if (widthScale != 1.0F || heightScale != 1.0F)
		{
			final Direction facing = entity.getAttachedFace().getOpposite();
			
			final double xLength = box.getXLength() / -2.0D;
			final double yLength = box.getYLength() / -2.0D;
			final double zLength = box.getZLength() / -2.0D;
			
			final double dX = xLength * (1.0D - widthScale); 
			final double dY = yLength * (1.0D - heightScale); 
			final double dZ = zLength * (1.0D - widthScale); 
			box = box.expand(dX, dY, dZ);
			box = box.offset(dX * facing.getOffsetX(), dY * facing.getOffsetY(), dZ * facing.getOffsetZ());
		}
		
		return box;
	}
}
