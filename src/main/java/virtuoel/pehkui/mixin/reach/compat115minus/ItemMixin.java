package virtuoel.pehkui.mixin.reach.compat115minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(Item.class)
public class ItemMixin
{
	/*
	@Redirect(method = MixinConstants.RAYCAST, at = @At(value = "INVOKE", target = MixinConstants.GET_VALUE, remap = false), remap = false)
	private static double raycastModifyMultiplier(EntityAttributeInstance reach, World world, PlayerEntity player, RaycastContext.FluidHandling fluidHandling)
	{
		final float scale = ScaleUtils.getReachScale(player);
		
		if (scale != 1.0F)
		{
			return reach.getValue() * scale;
		}
		
		return reach.getValue();
	}
	*/
}
