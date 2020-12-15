package virtuoel.pehkui.mixin.compat116plus;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(SpawnEggItem.class)
public class SpawnEggItemMixin
{
	@Inject(at = @At("RETURN"), method = "spawnBaby(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/entity/mob/MobEntity;Lnet/minecraft/entity/EntityType;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/item/ItemStack;)Ljava/util/Optional;")
	private void onSpawnBaby(PlayerEntity user, MobEntity mobEntity, EntityType<? extends MobEntity> entityType, ServerWorld serverWorld, Vec3d vec3d, ItemStack itemStack, CallbackInfoReturnable<Optional<MobEntity>> info)
	{
		info.getReturnValue().ifPresent(e ->
		{
			ScaleUtils.loadScale(e, mobEntity);
		});
	}
}
