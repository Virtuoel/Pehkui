package virtuoel.pehkui.mixin.compat116minus;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(Entity.class)
public abstract class EntityCalculateDimensionsMixin
{
	@Shadow World world;
	@Shadow @Final @Mutable EntityType<?> type;
	@Shadow abstract void move(MovementType type, Vec3d movement);
	
	@Inject(method = "calculateDimensions", at = @At(value = "INVOKE", shift = Shift.AFTER, ordinal = 1, target = "Lnet/minecraft/entity/Entity;setBoundingBox(Lnet/minecraft/util/math/Box;)V"))
	private void pehkui$calculateDimensions(CallbackInfo info, @Local(ordinal = 0) EntityDimensions previous, @Local(ordinal = 1) EntityDimensions current)
	{
		if (this.world.isClient && type == EntityType.PLAYER && current.width > previous.width)
		{
			final float scale = ScaleUtils.getBoundingBoxWidthScale((Entity) (Object) this);
			final float dist = (previous.width - current.width) / 2.0F;
			
			move(MovementType.SELF, new Vec3d(dist / scale, 0.0D, dist / scale));
		}
	}
	
	@ModifyVariable(method = "calculateDimensions", at = @At("STORE"))
	private float pehkui$calculateDimensions$vector(float value)
	{
		final float scale = ScaleUtils.getBoundingBoxWidthScale((Entity) (Object) this);
		
		return (scale != 1.0F ? value / scale : value) / 2.0F;
	}
}
