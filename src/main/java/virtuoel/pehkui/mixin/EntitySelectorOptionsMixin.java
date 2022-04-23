package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.command.EntitySelectorOptions;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import virtuoel.pehkui.util.PehkuiEntityExtensions;

@Mixin(EntitySelectorOptions.class)
public class EntitySelectorOptionsMixin
{
	@Inject(method = "method_9957", at = @At(value = "INVOKE", shift = Shift.BEFORE, target = "Lnet/minecraft/entity/Entity;writeNbt(Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/nbt/NbtCompound;"))
	private static void onNbtSelectorPre(NbtCompound nbt, boolean negated, Entity entity, CallbackInfoReturnable<Boolean> info)
	{
		((PehkuiEntityExtensions) entity).pehkui_setShouldIgnoreScaleNbt(true);
	}
	
	@Inject(method = "method_9957", at = @At(value = "INVOKE", shift = Shift.AFTER, target = "Lnet/minecraft/entity/Entity;writeNbt(Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/nbt/NbtCompound;"))
	private static void onNbtSelectorPost(NbtCompound nbt, boolean negated, Entity entity, CallbackInfoReturnable<Boolean> info)
	{
		((PehkuiEntityExtensions) entity).pehkui_setShouldIgnoreScaleNbt(false);
	}
}
