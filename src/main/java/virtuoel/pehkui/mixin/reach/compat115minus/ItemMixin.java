package virtuoel.pehkui.mixin.reach.compat115minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(Item.class)
public class ItemMixin
{
	@ModifyConstant(target = @Desc(value = MixinConstants.RAYCAST, args = { World.class, PlayerEntity.class, RaycastContext.FluidHandling.class }, ret = HitResult.class), constant = @Constant(doubleValue = 5.0D), remap = false)
	private static double raycastModifyMultiplier(double value, World world, PlayerEntity player, RaycastContext.FluidHandling fluidHandling)
	{
		final float scale = ScaleUtils.getBlockReachScale(player);
		
		if (scale != 1.0F)
		{
			return value * scale;
		}
		
		return value;
	}
}
