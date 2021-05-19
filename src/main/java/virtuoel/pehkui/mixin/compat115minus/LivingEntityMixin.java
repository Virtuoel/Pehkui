package virtuoel.pehkui.mixin.compat115minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin
{
	@ModifyConstant(method = "travel", constant = @Constant(floatValue = 4.0F))
	private float modifyLimbDistance(float value)
	{
		return ScaleUtils.modifyLimbDistance(value, (Entity) (Object) this);
	}
	
	@Unique Vec3d initialClimbingPos = null;
	
	@Inject(method = "isClimbing()Z", at = @At(value = "RETURN"), cancellable = true)
	private void onIsClimbing(CallbackInfoReturnable<Boolean> info)
	{
		final LivingEntity self = (LivingEntity) (Object) this;
		
		if (initialClimbingPos != null || info.getReturnValueZ() || self.isSpectator())
		{
			return;
		}
		
		final float width = ScaleUtils.getWidthScale(self);
		
		if (width > 1.0F && !ScaleUtils.isAboveCollisionThreshold(self))
		{
			final Box bounds = self.getBoundingBox();
			
			final double halfUnscaledXLength = (bounds.getXLength() / width) / 2.0D;
			final int minX = MathHelper.floor(bounds.minX + halfUnscaledXLength);
			final int maxX = MathHelper.floor(bounds.maxX - halfUnscaledXLength);
			
			final int minY = MathHelper.floor(bounds.minY);
			
			final double halfUnscaledZLength = (bounds.getZLength() / width) / 2.0D;
			final int minZ = MathHelper.floor(bounds.minZ + halfUnscaledZLength);
			final int maxZ = MathHelper.floor(bounds.maxZ - halfUnscaledZLength);
			
			initialClimbingPos = method_5812();
			
			for (final BlockPos pos : BlockPos.iterate(minX, minY, minZ, maxX, minY, maxZ))
			{
				setPosDirectly(pos.getX(), pos.getY(), pos.getZ());
				if (self.isClimbing())
				{
					info.setReturnValue(true);
					break;
				}
			}
			
			setPosDirectly(initialClimbingPos.getX(), initialClimbingPos.getY(), initialClimbingPos.getZ());
			initialClimbingPos = null;
		}
	}
}
