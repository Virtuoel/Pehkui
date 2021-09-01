package virtuoel.pehkui.mixin.reach;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin({
	AbstractFurnaceBlockEntity.class,
	BrewingStandBlockEntity.class,
	LootableContainerBlockEntity.class,
})
public abstract class BlockEntityUseDistanceMixin
{
	@ModifyConstant(method = "canPlayerUse", constant = @Constant(doubleValue = 64.0D))
	private double canPlayerUseModifyDistance(double value, PlayerEntity player)
	{
		final float scale = ScaleUtils.getBlockReachScale(player);
		return scale != 1.0F ? scale * scale * value : value;
	}
}
