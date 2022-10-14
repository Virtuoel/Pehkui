package virtuoel.pehkui.mixin.compat117;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.world.entity.EntityLike;
import net.minecraft.world.entity.SectionedEntityCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(SectionedEntityCache.class)
public class SectionedEntityCacheMixin
{
	@Redirect(method = "method_31776", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/EntityLike;getBoundingBox()Lnet/minecraft/util/math/Box;"))
	private static Box pehkui$intersecting$applyInteractionHitbox(EntityLike obj)
	{
		if (obj instanceof Entity)
		{
			Entity entity = (Entity) obj;
			final float interactionWidth = ScaleUtils.getInteractionWidthScale(entity);
			final float interactionHeight = ScaleUtils.getInteractionHeightScale(entity);

			if (interactionWidth != 1.0F || interactionHeight != 1.0F)
			{
				final double scaledWidth = (entity.getWidth() * interactionWidth * 0.30000001192092896D) - (entity.getWidth() * 0.30000001192092896D);
				final double scaledHeight = (entity.getHeight() * interactionHeight * 0.30000001192092896D) - (entity.getHeight() * 0.30000001192092896D);

				return entity.getBoundingBox().expand(scaledWidth, scaledHeight, scaledWidth);
			}
		}
		return obj.getBoundingBox();
	}
}
