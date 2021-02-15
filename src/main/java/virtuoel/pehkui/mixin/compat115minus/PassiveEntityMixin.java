package virtuoel.pehkui.mixin.compat115minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(PassiveEntity.class)
public class PassiveEntityMixin
{
	@Inject(method = "method_5992(Lnet/minecraft/class_1657;Lnet/minecraft/class_1268;)Z", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", shift = Shift.BEFORE, target = "Lnet/minecraft/class_1296;method_5808(DDDFF)V", remap = false), remap = false)
	private void onInteractMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<Boolean> info, ItemStack itemStack, Item item, PassiveEntity passiveEntity)
	{
		ScaleUtils.loadScale(passiveEntity, (Entity) (Object) this);
	}
}
