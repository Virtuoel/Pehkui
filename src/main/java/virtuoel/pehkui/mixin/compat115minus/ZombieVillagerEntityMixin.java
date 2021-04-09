package virtuoel.pehkui.mixin.compat115minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ZombieVillagerEntity.class)
public class ZombieVillagerEntityMixin
{
	@Inject(method = MixinConstants.FINISH_CONVERSION, locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", shift = Shift.BEFORE, target = MixinConstants.VILLAGER_COPY_POS_AND_ROT, remap = false), remap = false)
	private void onFinishConversion(ServerWorld world, CallbackInfo info, VillagerEntity villagerEntity)
	{
		ScaleUtils.loadScale(villagerEntity, (Entity) (Object) this);
	}
}
