package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(EnderChestBlockEntity.class)
public abstract class EnderChestBlockEntityMixin
{
	@ModifyConstant(method = "canPlayerUse", constant = @Constant(doubleValue = 0.5D, ordinal = 0))
	private double canPlayerUseModifyXOffset(double value, PlayerEntity player)
	{
		return ScaleUtils.getBlockXOffset(((BlockEntity) (Object) this).getPos(), player);
	}
	
	@ModifyConstant(method = "canPlayerUse", constant = @Constant(doubleValue = 0.5D, ordinal = 1))
	private double canPlayerUseModifyYOffset(double value, PlayerEntity player)
	{
		return ScaleUtils.getBlockYOffset(((BlockEntity) (Object) this).getPos(), player) - player.getStandingEyeHeight();
	}
	
	@ModifyConstant(method = "canPlayerUse", constant = @Constant(doubleValue = 0.5D, ordinal = 2))
	private double canPlayerUseModifyZOffset(double value, PlayerEntity player)
	{
		return ScaleUtils.getBlockZOffset(((BlockEntity) (Object) this).getPos(), player);
	}
}
