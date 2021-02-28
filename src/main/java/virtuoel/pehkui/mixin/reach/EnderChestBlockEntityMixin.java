package virtuoel.pehkui.mixin.reach;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(EnderChestBlockEntity.class)
public abstract class EnderChestBlockEntityMixin
{
	@ModifyConstant(method = "canPlayerUse", constant = @Constant(doubleValue = 64.0D))
	private double canPlayerUseModifyDistance(double value, PlayerEntity player)
	{
		final float scale = ScaleUtils.getReachScale(player);
		return scale != 1.0F ? scale * scale * value : value;
	}
}
