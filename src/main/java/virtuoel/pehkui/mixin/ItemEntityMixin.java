package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ItemEntity.class)
public class ItemEntityMixin
{
	@ModifyArgs(method = "tryMerge()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
	private void tryMergeModifyWidth(Args args)
	{
		final float scale = ScaleUtils.getWidthScale((Entity) (Object) this);
		if (scale == 1.0F)
			return;
		int dx = args.size() - 3;
		int dz = args.size() - 1;
		args.set(dx, args.<Double>get(dx) * scale);
		args.set(dz, args.<Double>get(dz) * scale);
	}
}
