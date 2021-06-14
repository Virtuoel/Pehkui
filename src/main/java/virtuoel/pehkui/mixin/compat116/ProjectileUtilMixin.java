package virtuoel.pehkui.mixin.compat116;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.math.Box;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ProjectileUtil.class)
public class ProjectileUtilMixin
{
	@Redirect(method = "getEntityCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getBoundingBox()Lnet/minecraft/util/math/Box;"))
	private static Box getEntityCollisionModifyGetBoundingBox(Entity obj)
	{
		final float width = ScaleUtils.getWidthScale(obj);
		final float height = ScaleUtils.getHeightScale(obj);
		
		if (width != 1.0F || height != 1.0F)
		{
			final double scaledWidth = (width * 0.30000001192092896D) - 0.30000001192092896D;
			final double scaledHeight = (height * 0.30000001192092896D) - 0.30000001192092896D;
			return obj.getBoundingBox().expand(scaledWidth, scaledHeight, scaledWidth);
		}
		
		return obj.getBoundingBox();
	}
}
