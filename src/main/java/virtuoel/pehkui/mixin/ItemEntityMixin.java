package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.Box;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ItemEntity.class)
public class ItemEntityMixin
{
	@ModifyArg(method = "tryMerge", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getEntitiesByClass(Ljava/lang/Class;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;)Ljava/util/List;"))
	private Box tryMergeModifyBounds(Box box)
	{
		final ItemEntity self = (ItemEntity) (Object) this;
		final Box bounds = self.getBoundingBox();
		
		final double xExpand = box.getXLength() - bounds.getXLength();
		final double yExpand = box.getYLength() - bounds.getYLength();
		final double zExpand = box.getZLength() - bounds.getZLength();
		final float widthScale = ScaleUtils.getBoundingBoxWidthScale(self);
		final float heightScale = ScaleUtils.getBoundingBoxHeightScale(self);
		
		return bounds.expand(
			widthScale != 1.0F ? widthScale * xExpand : xExpand,
			heightScale != 1.0F ? heightScale * yExpand : yExpand,
			widthScale != 1.0F ? widthScale * zExpand : zExpand);
	}
}
