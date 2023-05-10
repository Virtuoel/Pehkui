package virtuoel.pehkui.mixin.compat117plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;

@Mixin(Entity.class)
public abstract class EntityCalculateDimensionsMixin
{
	@Inject(method = "calculateDimensions", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", shift = Shift.AFTER, target = "Lnet/minecraft/entity/Entity;refreshPosition()V"))
	private void pehkui$calculateDimensions(CallbackInfo info, EntityDimensions previous, EntityPose pose, EntityDimensions current)
	{
		final Entity self = ((Entity) (Object) this);
		final World world = self.getEntityWorld();
		
		if (world.isClient && self.getType() == EntityType.PLAYER && current.width > previous.width)
		{
			final double prevW = Math.min(previous.width, 4.0D);
			final double prevH = Math.min(previous.height, 4.0D);
			final double currW = Math.min(current.width, 4.0D);
			final double currH = Math.min(current.height, 4.0D);
			final Vec3d lastCenter = self.getPos().add(0.0D, prevH / 2.0D, 0.0D);
			final double w = Math.max(0.0F, currW - prevW) + 1.0E-6D;
			final double h = Math.max(0.0F, currH - prevH) + 1.0E-6D;
			final VoxelShape voxelShape = VoxelShapes.cuboid(Box.of(lastCenter, w, h, w));
			world.findClosestCollision(self, voxelShape, lastCenter, currW, currH, currW)
				.ifPresent(vec -> self.setPosition(vec.add(0.0D, -currH / 2.0D, 0.0D)));
		}
	}
}
