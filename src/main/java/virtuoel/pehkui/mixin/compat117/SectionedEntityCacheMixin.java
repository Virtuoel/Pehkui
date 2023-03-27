package virtuoel.pehkui.mixin.compat117;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.world.entity.EntityLike;
import net.minecraft.world.entity.SectionedEntityCache;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(SectionedEntityCache.class)
public class SectionedEntityCacheMixin
{
	@Redirect(method = "method_31776", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/EntityLike;getBoundingBox()Lnet/minecraft/util/math/Box;"))
	private static Box pehkui$intersecting$getBoundingBox(EntityLike obj)
	{
		final Box bounds = obj.getBoundingBox();
		
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
