package virtuoel.pehkui.mixin.compat1203plus;

import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.entity.Entity;
import net.minecraft.item.BrushItem;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(BrushItem.class)
public class BrushItemMixin
{
	@ModifyArg(method = "getHitResult", index = 2, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileUtil;getCollision(Lnet/minecraft/entity/Entity;Ljava/util/function/Predicate;D)Lnet/minecraft/util/hit/HitResult;"))
	private double pehkui$getCollision$expand(Entity entity, Predicate<Entity> predicate, double range)
	{
		final float scale = ScaleUtils.getBlockReachScale(entity);
		
		return scale != 1.0F ? range * scale : range;
	}
}
