package virtuoel.pehkui.mixin.reach.compat1194plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(Inventory.class)
public interface InventoryMixin
{
	@Overwrite
	public static boolean canPlayerUse(BlockEntity blockEntity, PlayerEntity player, int range)
	{
		World world = blockEntity.getWorld();
		BlockPos pos = blockEntity.getPos();
		
		if (world == null)
		{
			return false;
		}
		else if (world.getBlockEntity(pos) != blockEntity)
		{
			return false;
		}
		else
		{
			double x = ((double) pos.getX()) + 0.5D;
			double y = ((double) pos.getY()) + 0.5D;
			double z = ((double) pos.getZ()) + 0.5D;
			final Vec3d eyePos = player.getEyePos();
			x = (x - 0.5D) + ScaleUtils.getBlockXOffset(pos, player) - (eyePos.getX() - player.getX());
			y = (y - 0.5D) + ScaleUtils.getBlockYOffset(pos, player) - (eyePos.getY() - player.getY());
			z = (z - 0.5D) + ScaleUtils.getBlockZOffset(pos, player) - (eyePos.getZ() - player.getZ());
			final double reach = ScaleUtils.getBlockReachScale(player) * (double) range;
			return player.squaredDistanceTo(x, y, z) <= reach * reach;
		}
	}
}
