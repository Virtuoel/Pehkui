package virtuoel.pehkui.mixin.compat116plus.compat118minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

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
	@ModifyConstant(method = MixinConstants.GIVE_TO_VEC3D, constant = @Constant(doubleValue = 0.3F, ordinal = 0))
	private static double giveModifyOffset(double value, LivingEntity entity, ItemStack stack, Vec3d targetLocation)
	{
		final float scale = ScaleUtils.getEyeHeightScale(entity);
		
		return scale != 1.0F ? scale * value : value;
	}
	
	@Inject(method = MixinConstants.GIVE_TO_VEC3D, locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ItemEntity;setToDefaultPickupDelay()V"))
	private static void onGive(LivingEntity entity, ItemStack stack, Vec3d targetLocation, CallbackInfo info, double d, ItemEntity itemEntity, float f, Vec3d vec3d)
	{
		ScaleUtils.setScaleOfDrop(itemEntity, entity);
	}
}
