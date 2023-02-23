package virtuoel.pehkui.mixin.compat119plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import virtuoel.pehkui.util.ScaleUtils;

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
	/*
	@Redirect(method = "onPlayerInteractEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;squaredDistanceTo(Lnet/minecraft/util/math/Vec3d;)D"))
	private double pehkui$onPlayerInteractEntity$squaredDistanceTo(Entity entity, Vec3d eyePos)
	{
		final float margin = entity.getTargetingMargin();
		
		Box box = entity.getBoundingBox().expand(margin);
		
		final float interactionWidth = ScaleUtils.getInteractionBoxWidthScale(entity);
		final float interactionHeight = ScaleUtils.getInteractionBoxHeightScale(entity);
		
		if (interactionWidth != 1.0F || interactionHeight != 1.0F)
		{
			final double scaledXLength = box.getXLength() * 0.5D * (interactionWidth - 1.0F);
			final double scaledYLength = box.getYLength() * 0.5D * (interactionHeight - 1.0F);
			final double scaledZLength = box.getZLength() * 0.5D * (interactionWidth - 1.0F);
			final double scaledMarginWidth = margin * (interactionWidth - 1.0F);
			final double scaledMarginHeight = margin * (interactionHeight - 1.0F);
			
			box = box.expand(scaledXLength + scaledMarginWidth, scaledYLength + scaledMarginHeight, scaledZLength + scaledMarginWidth);
		}
		
		final double nearestX;
		if (eyePos.x < box.minX)
		{
			nearestX = box.minX;
		}
		else if (eyePos.x > box.maxX)
		{
			nearestX = box.maxX;
		}
		else
		{
			nearestX = eyePos.x;
		}
		
		final double nearestY;
		if (eyePos.y < box.minY)
		{
			nearestY = box.minY;
		}
		else if (eyePos.y > box.maxY)
		{
			nearestY = box.maxY;
		}
		else
		{
			nearestY = eyePos.y;
		}
		
		final double nearestZ;
		if (eyePos.z < box.minZ)
		{
			nearestZ = box.minZ;
		}
		else if (eyePos.z > box.maxZ)
		{
			nearestZ = box.maxZ;
		}
		else
		{
			nearestZ = eyePos.z;
		}
		
		return eyePos.squaredDistanceTo(nearestX, nearestY, nearestZ);
	}
	*/
}
