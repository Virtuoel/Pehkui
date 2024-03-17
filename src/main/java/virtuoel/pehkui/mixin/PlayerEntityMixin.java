package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin
{
	@ModifyReturnValue(method = "getDimensions", at = @At("RETURN"))
	private EntityDimensions pehkui$getDimensions(EntityDimensions original)
	{
		final float widthScale = ScaleUtils.getBoundingBoxWidthScale((Entity) (Object) this);
		final float heightScale = ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this);
		
		if (widthScale != 1.0F || heightScale != 1.0F)
		{
			return original.scaled(widthScale, heightScale);
		}
		
		return original;
	}
	
	@Inject(at = @At("RETURN"), method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;")
	private void pehkui$dropItem(ItemStack stack, boolean spread, boolean thrown, CallbackInfoReturnable<ItemEntity> info)
	{
		final ItemEntity entity = info.getReturnValue();
		
		if (entity != null)
		{
			ScaleUtils.setScaleOfDrop(entity, (Entity) (Object) this);
			
			final float scale = ScaleUtils.getEyeHeightScale((Entity) (Object) this);
			
			if (scale != 1.0F)
			{
				final Vec3d pos = entity.getPos();
				
				entity.setPosition(pos.x, pos.y + ((1.0F - scale) * 0.3D), pos.z);
			}
		}
	}
	
	@WrapOperation(method = "tickMovement()V", at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
	private Box pehkui$tickMovement$expand(Box obj, double x, double y, double z, Operation<Box> original)
	{
		final float widthScale = ScaleUtils.getBoundingBoxWidthScale((Entity) (Object) this);
		final float heightScale = ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this);
		
		if (widthScale != 1.0F)
		{
			x *= widthScale;
			z *= widthScale;
		}
		
		if (heightScale != 1.0F)
		{
			y *= heightScale;
		}
		
		return original.call(obj, x, y, z);
	}
	
	@ModifyExpressionValue(method = "attack(Lnet/minecraft/entity/Entity;)V", at = { @At(value = "CONSTANT", args = "floatValue=0.5F", ordinal = 1), @At(value = "CONSTANT", args = "floatValue=0.5F", ordinal = 2), @At(value = "CONSTANT", args = "floatValue=0.5F", ordinal = 3) })
	private float pehkui$attack$knockback(float value)
	{
		final float scale = ScaleUtils.getKnockbackScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
	
	@ModifyExpressionValue(method = "getAttackCooldownProgressPerTick", at = @At(value = "CONSTANT", args = "doubleValue=20.0D"))
	private double pehkui$getAttackCooldownProgressPerTick$multiplier(double value)
	{
		final float scale = ScaleUtils.getAttackSpeedScale((Entity) (Object) this);
		
		return scale != 1.0F ? value / scale : value;
	}
	
	@Inject(at = @At("RETURN"), target = @Desc(value = "getDigSpeed", args = { BlockState.class, BlockPos.class }, ret = float.class), cancellable = true)
	private void pehkui$getBlockBreakingSpeed(BlockState block, BlockPos pos, CallbackInfoReturnable<Float> info)
	{
		final float scale = ScaleUtils.getMiningSpeedScale((Entity) (Object) this);
		
		if (scale != 1.0F)
		{
			info.setReturnValue(info.getReturnValueF() * scale);
		}
	}
	
	@ModifyExpressionValue(method = "updateCapeAngles", at = { @At(value = "CONSTANT", args = "doubleValue=10.0D"), @At(value = "CONSTANT", args = "doubleValue=-10.0D") })
	private double pehkui$updateCapeAngles$limits(double value)
	{
		final float scale = ScaleUtils.getMotionScale((Entity) (Object) this);
		
		return scale != 1.0F ? scale * value : value;
	}
	
	@WrapOperation(method = "attack(Lnet/minecraft/entity/Entity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
	private Box pehkui$attack$expand(Box obj, double x, double y, double z, Operation<Box> original, @Local(argsOnly = true) Entity target)
	{
		final float widthScale = ScaleUtils.getBoundingBoxWidthScale(target);
		final float heightScale = ScaleUtils.getBoundingBoxHeightScale(target);
		
		if (widthScale != 1.0F)
		{
			x *= widthScale;
			z *= widthScale;
		}
		
		if (heightScale != 1.0F)
		{
			y *= heightScale;
		}
		
		return original.call(obj, x, y, z);
	}
}
