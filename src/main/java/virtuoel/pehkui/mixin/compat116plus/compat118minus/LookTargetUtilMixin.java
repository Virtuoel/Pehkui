package virtuoel.pehkui.mixin.compat116plus.compat118minus;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(LookTargetUtil.class)
public class LookTargetUtilMixin
{
	@Dynamic
	@ModifyExpressionValue(method = MixinConstants.GIVE_TO_VEC3D, at = @At(value = "CONSTANT", args = "floatValue=0.3F", ordinal = 0))
	private static float pehkui$give$offset(float value, LivingEntity entity, ItemStack stack, Vec3d targetLocation)
	{
		final float scale = ScaleUtils.getEyeHeightScale(entity);
		
		return scale != 1.0F ? scale * value : value;
	}
	
	@Dynamic
	@Inject(method = MixinConstants.GIVE_TO_VEC3D, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ItemEntity;setToDefaultPickupDelay()V"))
	private static void pehkui$give(LivingEntity entity, ItemStack stack, Vec3d targetLocation, CallbackInfo info, @Local ItemEntity itemEntity)
	{
		ScaleUtils.setScaleOfDrop(itemEntity, entity);
	}
}
