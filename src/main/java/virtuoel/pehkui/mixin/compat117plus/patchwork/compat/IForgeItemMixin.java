package virtuoel.pehkui.mixin.compat117plus.patchwork.compat;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(IItemExtension.class)
public interface IForgeItemMixin
{
	@Overwrite(remap = false)
	@NotNull
	default Box getSweepHitBox(@NotNull ItemStack stack, @NotNull PlayerEntity player, @NotNull Entity target)
	{
		final float width = ScaleUtils.getBoundingBoxWidthScale(target);
		return target.getBoundingBox().expand(width, ScaleUtils.getBoundingBoxHeightScale(target), width);
	}
}
