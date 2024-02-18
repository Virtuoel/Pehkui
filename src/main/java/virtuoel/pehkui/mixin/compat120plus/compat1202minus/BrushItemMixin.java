package virtuoel.pehkui.mixin.compat120plus.compat1202minus;

import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.entity.Entity;
import net.minecraft.item.BrushItem;
import net.minecraft.util.hit.HitResult;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(BrushItem.class)
public class BrushItemMixin
{
	@Dynamic
	@WrapOperation(method = MixinConstants.GET_HIT_RESULT, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileUtil;getCollision(Lnet/minecraft/entity/Entity;Ljava/util/function/Predicate;D)Lnet/minecraft/util/hit/HitResult;"))
	private HitResult pehkui$getHitResult$getCollision(Entity entity, Predicate<Entity> predicate, double range, Operation<HitResult> original)
	{
		final float scale = ScaleUtils.getBlockReachScale(entity);
		
		return original.call(entity, predicate, scale != 1.0F ? range * scale : range);
	}
}
