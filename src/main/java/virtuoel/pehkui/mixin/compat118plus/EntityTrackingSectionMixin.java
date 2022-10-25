package virtuoel.pehkui.mixin.compat118plus;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.world.entity.EntityLike;
import net.minecraft.world.entity.EntityTrackingSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(EntityTrackingSection.class)
public class EntityTrackingSectionMixin
{
	@Redirect(method = "forEach(Lnet/minecraft/util/math/Box;Ljava/util/function/Consumer;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/EntityLike;getBoundingBox()Lnet/minecraft/util/math/Box;"))
	private Box pehkui$forEach$applyInteractionHitbox(EntityLike obj)
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

	@Redirect(method = "forEach(Lnet/minecraft/util/TypeFilter;Lnet/minecraft/util/math/Box;Ljava/util/function/Consumer;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/EntityLike;getBoundingBox()Lnet/minecraft/util/math/Box;"))
	private Box pehkui$forEach$applyInteractionHitboxFiltered(EntityLike obj)
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
