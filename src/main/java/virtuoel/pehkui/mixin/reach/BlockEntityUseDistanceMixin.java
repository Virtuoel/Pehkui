package virtuoel.pehkui.mixin.reach;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(value = {
	AbstractFurnaceBlockEntity.class,
	BrewingStandBlockEntity.class,
	LootableContainerBlockEntity.class,
}, priority = 1010)
public abstract class BlockEntityUseDistanceMixin
{
	@ModifyConstant(method = "canPlayerUse", require = 0, expect = 0, constant = @Constant(doubleValue = 64.0D))
	private double pehkui$canPlayerUse$distance(double value, PlayerEntity player)
	{
		final float scale = ScaleUtils.getBlockReachScale(player);
		return scale != 1.0F ? scale * scale * value : value;
	}
}
