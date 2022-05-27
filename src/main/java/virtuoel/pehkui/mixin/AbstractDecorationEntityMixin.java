package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(AbstractDecorationEntity.class)
public abstract class AbstractDecorationEntityMixin
{
	@Shadow
	protected abstract void updateAttachmentPosition();
	
	@Inject(at = @At("RETURN"), method = "calculateDimensions")
	private void onCalculateDimensions(CallbackInfo info)
	{
		updateAttachmentPosition();
	}
	
	@ModifyVariable(method = "dropStack(Lnet/minecraft/item/ItemStack;F)Lnet/minecraft/entity/ItemEntity;", at = @At(value = "STORE"))
	private ItemEntity onDropStack(ItemEntity entity)
	{
		ScaleUtils.setScaleOfDrop(entity, (Entity) (Object) this);
		return entity;
	}
	
	@ModifyArg(method = "updateAttachmentPosition()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/decoration/AbstractDecorationEntity;setBoundingBox(Lnet/minecraft/util/math/Box;)V"))
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
