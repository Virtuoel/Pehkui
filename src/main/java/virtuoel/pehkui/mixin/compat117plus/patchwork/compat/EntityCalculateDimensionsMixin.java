package virtuoel.pehkui.mixin.compat117plus.patchwork.compat;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Coerce;
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
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(value = Entity.class, priority = 1010)
public abstract class EntityCalculateDimensionsMixin
{
	@Shadow
	protected boolean firstUpdate;
	
	@Inject(method = "calculateDimensions", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", shift = Shift.AFTER, target = "Lnet/minecraft/entity/Entity;refreshPosition()V"))
	private void onCalculateDimensions(CallbackInfo info, EntityDimensions previous, EntityPose pose, @Coerce Object sizeEvent, EntityDimensions current)
	{
		final Entity self = (Entity) (Object) this;
		
		if (self.world.isClient && self.getType() == EntityType.PLAYER && current.width > previous.width && !ScaleUtils.isAboveCollisionThreshold(self))
		{
			final Vec3d lastCenter = self.getPos().add(0.0D, previous.height / 2.0D, 0.0D);
			final double w = Math.max(0.0F, current.width - previous.width) + 1.0E-6D;
			final double h = Math.max(0.0F, current.height - previous.height) + 1.0E-6D;
			final VoxelShape voxelShape = VoxelShapes.cuboid(Box.of(lastCenter, w, h, w));
			self.world.findClosestCollision(self, voxelShape, lastCenter, current.width, current.height, current.width)
			.ifPresent(vec -> self.setPosition(vec.add(0.0D, (-current.height) / 2.0D, 0.0D)));
		}
	}
}
