package virtuoel.pehkui.mixin.compat115minus;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin
{
	@Dynamic
	@ModifyConstant(method = MixinConstants.TRAVEL, constant = @Constant(floatValue = 4.0F))
	private float pehkui$travel$limbDistance(float value)
	{
		return ScaleUtils.modifyLimbDistance(value, (Entity) (Object) this);
	}
	
	@Unique Vec3d pehkui$initialClimbingPos = null;
	
	@ModifyReturnValue(method = "isClimbing()Z", at = @At("RETURN"))
	private boolean pehkui$isClimbing(boolean original)
	{
		final LivingEntity self = (LivingEntity) (Object) this;
		
		if (pehkui$initialClimbingPos != null || original || self.isSpectator())
		{
			return original;
		}
		
		final float width = ScaleUtils.getBoundingBoxWidthScale(self);
		
		if (width > 1.0F)
		{
			final Box bounds = self.getBoundingBox();
			
			final double halfUnscaledXLength = (bounds.getLengthX() / width) / 2.0D;
			final int minX = MathHelper.floor(bounds.minX + halfUnscaledXLength);
			final int maxX = MathHelper.floor(bounds.maxX - halfUnscaledXLength);
			
			final int minY = MathHelper.floor(bounds.minY);
			
			final double halfUnscaledZLength = (bounds.getLengthZ() / width) / 2.0D;
			final int minZ = MathHelper.floor(bounds.minZ + halfUnscaledZLength);
			final int maxZ = MathHelper.floor(bounds.maxZ - halfUnscaledZLength);
			
			pehkui$initialClimbingPos = method_5812();
			
			for (final BlockPos pos : BlockPos.iterate(minX, minY, minZ, maxX, minY, maxZ))
			{
				setPosDirectly(pos.getX(), pos.getY(), pos.getZ());
				if (self.isClimbing())
				{
					return true;
				}
			}
			
			setPosDirectly(pehkui$initialClimbingPos.getX(), pehkui$initialClimbingPos.getY(), pehkui$initialClimbingPos.getZ());
			pehkui$initialClimbingPos = null;
		}
		
		return original;
	}
}
