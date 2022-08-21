package virtuoel.pehkui.mixin.compat117plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ShulkerEntity.class)
public class ShulkerEntityMixin
{
	@Inject(method = "calculateBoundingBox", at = @At("RETURN"), cancellable = true)
	private void pehkui$calculateBoundingBox(CallbackInfoReturnable<Box> info)
	{
		final ShulkerEntity entity = (ShulkerEntity) (Object) this;
		
		final Direction facing = entity.getAttachedFace().getOpposite();
		
		Box box = info.getReturnValue();
		
		final double xLength = box.getXLength() / -2.0D;
		final double yLength = box.getYLength() / -2.0D;
		final double zLength = box.getZLength() / -2.0D;
		
		final float widthScale = ScaleUtils.getBoundingBoxWidthScale(entity);
		final float heightScale = ScaleUtils.getBoundingBoxHeightScale(entity);
		
		if (widthScale != 1.0F || heightScale != 1.0F)
		{
			final double dX = xLength * (1.0D - widthScale); 
			final double dY = yLength * (1.0D - heightScale); 
			final double dZ = zLength * (1.0D - widthScale); 
			box = box.expand(dX, dY, dZ);
			box = box.offset(dX * facing.getOffsetX(), dY * facing.getOffsetY(), dZ * facing.getOffsetZ());
			
			info.setReturnValue(box);
		}
	}
}
