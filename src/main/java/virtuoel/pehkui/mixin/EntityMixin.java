package virtuoel.pehkui.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import virtuoel.pehkui.Pehkui;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.entity.ResizableEntity;

@Mixin(Entity.class)
public abstract class EntityMixin implements ResizableEntity
{
	@Shadow World world;
	
	@Shadow abstract void updatePosition(double x, double y, double z);
	
	public ScaleData pehkui_scaleData = new ScaleData(Optional.of(((Entity) (Object) this)::calculateDimensions));
	
	@Deprecated
	@Override
	public ScaleData pehkui_getScaleData()
	{
		return pehkui_scaleData;
	}
	
	@Inject(at = @At("HEAD"), method = "fromTag")
	private void onFromTag(CompoundTag tag, CallbackInfo info)
	{
		if (tag.contains(Pehkui.MOD_ID + ":scale_data", NbtType.COMPOUND))
		{
			pehkui_scaleData.fromTag(tag.getCompound(Pehkui.MOD_ID + ":scale_data"));
			
			if (pehkui_scaleData.getScale() != 1.0F && world != null && !world.isClient)
			{
				pehkui_scaleData.markForSync();
			}
		}
	}
	
	@Inject(at = @At("HEAD"), method = "toTag")
	private void onToTag(CompoundTag tag, CallbackInfoReturnable<CompoundTag> info)
	{
		tag.put(Pehkui.MOD_ID + ":scale_data", pehkui_scaleData.toTag(new CompoundTag()));
	}
	
	@Inject(at = @At("HEAD"), method = "tick")
	private void onTickPre(CallbackInfo info)
	{
		pehkui_scaleData.tick();
	}
	
	@Inject(at = @At("HEAD"), method = "onStartedTrackingBy")
	private void onOnStartedTrackingBy(ServerPlayerEntity player, CallbackInfo info)
	{
		if (pehkui_scaleData.getScale() != 1.0F)
		{
			player.networkHandler.sendPacket(new CustomPayloadS2CPacket(Pehkui.SCALE_PACKET, pehkui_scaleData.toPacketByteBuf(new PacketByteBuf(Unpooled.buffer()).writeUuid(((Entity) (Object) this).getUuid()))));
			pehkui_scaleData.scaleModified = false;
		}
	}
	
	@Inject(at = @At("RETURN"), method = "getDimensions", cancellable = true)
	private void onGetDimensions(EntityPose pose, CallbackInfoReturnable<EntityDimensions> info)
	{
		final float scale = pehkui_scaleData.getScale();
		
		if (scale != 1.0F)
		{
			info.setReturnValue(info.getReturnValue().scaled(scale));
		}
	}
	
	@Inject(method = "dropStack(Lnet/minecraft/item/ItemStack;F)Lnet/minecraft/entity/ItemEntity;", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ItemEntity;setToDefaultPickupDelay()V"))
	private void onDropStack(ItemStack stack, float yOffset, CallbackInfoReturnable<ItemEntity> info, ItemEntity entity)
	{
		final float scale = pehkui_scaleData.getScale();
		
		if (scale != 1.0F)
		{
			final ScaleData data = ScaleData.of(entity);
			data.setScale(scale);
			data.setTargetScale(scale);
			data.markForSync();
		}
	}
	
	@ModifyArg(method = "move", index = 0, at = @At(value = "INVOKE", target = "net/minecraft/entity/Entity.adjustMovementForSneaking(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/entity/MovementType;)Lnet/minecraft/util/math/Vec3d;"))
	private Vec3d onMoveAdjustMovementForSneakingProxy(Vec3d movement, MovementType type)
	{
		return type == MovementType.SELF || type == MovementType.PLAYER ? movement.multiply(pehkui_scaleData.getScale()) : movement;
	}
	
	@Inject(at = @At("HEAD"), method = "spawnSprintingParticles", cancellable = true)
	private void onSpawnSprintingParticles(CallbackInfo info)
	{
		if (pehkui_scaleData.getScale() < 1.0F)
		{
			info.cancel();
		}
	}
	
	@Redirect(method = "adjustMovementForCollisions", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;stepHeight:F"))
	private float adjustMovementForCollisionsStepHeightProxy(Entity obj)
	{
		return obj.stepHeight * pehkui_scaleData.getScale();
	}
}
