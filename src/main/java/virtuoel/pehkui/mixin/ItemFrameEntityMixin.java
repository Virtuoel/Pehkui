package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ItemFrameEntity.class)
public abstract class ItemFrameEntityMixin
{
	@ModifyArg(method = "updateAttachmentPosition()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/decoration/ItemFrameEntity;setBoundingBox(Lnet/minecraft/util/math/Box;)V"))
	private Box onUpdateAttachmentPositionModifyBox(Box box)
	{
		final AbstractDecorationEntity entity = (AbstractDecorationEntity) (Object) this;
		
		final Direction facing = entity.getHorizontalFacing();
		
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
		}
		
		return box;
	}
}
