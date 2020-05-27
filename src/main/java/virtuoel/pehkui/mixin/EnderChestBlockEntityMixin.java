package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import virtuoel.pehkui.api.ScaleData;

@Mixin(EnderChestBlockEntity.class)
public abstract class EnderChestBlockEntityMixin
{
	@ModifyConstant(method = "canPlayerUse", constant = @Constant(doubleValue = 0.5D, ordinal = 1))
	private double canPlayerUseModifyYOffset(double value, PlayerEntity player)
	{
		return value - player.getStandingEyeHeight();
	}
	
	@ModifyConstant(method = "canPlayerUse", constant = @Constant(doubleValue = 64.0D))
	private double canPlayerUseModifyDistance(double value, PlayerEntity player)
	{
		final float scale = ScaleData.of(player).getScale();
		return scale != 1.0F ? scale * scale * value : value;
	}
}
