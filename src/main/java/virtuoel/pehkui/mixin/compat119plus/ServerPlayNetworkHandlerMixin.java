package virtuoel.pehkui.mixin.compat119plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin
{
	@Shadow ServerPlayerEntity player;
	
	@ModifyArg(method = "onPlayerInteractBlock", index = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;squaredDistanceTo(Lnet/minecraft/util/math/Vec3d;)D"))
	private Vec3d pehkui$onPlayerInteractBlock$center(Vec3d center)
	{
		final BlockPos pos = new BlockPos(MathHelper.floor(center.x), MathHelper.floor(center.y), MathHelper.floor(center.z));
		final Vec3d eye = player.getEyePos();
		final BlockPos eyePos = new BlockPos(MathHelper.floor(eye.x), MathHelper.floor(eye.y), MathHelper.floor(eye.z));
		
		final double xOffset = eye.getX() - center.getX();
		final double yOffset = eye.getY() - center.getY();
		final double zOffset = eye.getZ() - center.getZ();
		
		center = center.add(
			eyePos.getX() == pos.getX() ? xOffset : xOffset < 0.0D ? -0.5D : 0.5D,
			eyePos.getY() == pos.getY() ? yOffset : yOffset < 0.0D ? -0.5D : 0.5D,
			eyePos.getZ() == pos.getZ() ? zOffset : zOffset < 0.0D ? -0.5D : 0.5D
		);
		
		return center;
	}
}
