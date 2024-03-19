package virtuoel.pehkui.mixin.compat117;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.world.entity.EntityLike;
import net.minecraft.world.entity.SectionedEntityCache;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(SectionedEntityCache.class)
public class SectionedEntityCacheMixin
{
	@Dynamic
	@WrapOperation(method = { "method_31776", "m_156874_" }, require = 0, expect = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/EntityLike;getBoundingBox()Lnet/minecraft/util/math/Box;"))
	private static Box pehkui$intersecting$getBoundingBox(EntityLike obj, Operation<Box> original)
	{
		final Box bounds = original.call(obj);
		
		if (obj instanceof Entity)
		{
			final Entity entity = (Entity) obj;
			
			final float interactionWidth = ScaleUtils.getInteractionBoxWidthScale(entity);
			final float interactionHeight = ScaleUtils.getInteractionBoxHeightScale(entity);
			
			if (interactionWidth != 1.0F || interactionHeight != 1.0F)
			{
				final double scaledXLength = bounds.getXLength() * 0.5D * (interactionWidth - 1.0F);
				final double scaledYLength = bounds.getYLength() * 0.5D * (interactionHeight - 1.0F);
				final double scaledZLength = bounds.getZLength() * 0.5D * (interactionWidth - 1.0F);
				
				return bounds.expand(scaledXLength, scaledYLength, scaledZLength);
			}
		}
		
		return bounds;
	}
}
