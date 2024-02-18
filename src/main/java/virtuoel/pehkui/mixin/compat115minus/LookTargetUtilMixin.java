package virtuoel.pehkui.mixin.compat115minus;

import org.spongepowered.asm.mixin.Dynamic;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(LookTargetUtil.class)
public class LookTargetUtilMixin
{
	@Dynamic
	@ModifyConstant(method = MixinConstants.GIVE_TO_TARGET, constant = @Constant(doubleValue = 0.3F, ordinal = 0))
	private static double pehkui$give$offset(double value, LivingEntity entity, ItemStack stack, LivingEntity target)
	{
		final float scale = ScaleUtils.getEyeHeightScale(entity);
		
		return scale != 1.0F ? scale * value : value;
	}
	
	@Dynamic
	@Inject(method = MixinConstants.GIVE_TO_TARGET, locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ItemEntity;setToDefaultPickupDelay()V"))
	private static void pehkui$give(LivingEntity entity, ItemStack stack, LivingEntity target, CallbackInfo info, double d, ItemEntity itemEntity, BlockPos blockPos, BlockPos blockPos2, float f, Vec3d vec3d)
	{
		ScaleUtils.setScaleOfDrop(itemEntity, entity);
	}
}
