package virtuoel.pehkui.mixin.reach.compat116plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(Item.class)
public class ItemMixin
{
	@ModifyVariable(method = "raycast", ordinal = 1, at = @At(value = "STORE"))
	private static Vec3d pehkui$raycast$end(Vec3d value, World world, PlayerEntity player, RaycastContext.FluidHandling fluidHandling)
	{
		final float scale = ScaleUtils.getBlockReachScale(player);
		
		if (scale != 1.0F)
		{
			final Vec3d eyePos = ScaleUtils.getEyePos(player);
			final Vec3d distance = value.subtract(eyePos);
			
			return eyePos.add(distance.multiply(scale));
		}
		
		return value;
	}
}
