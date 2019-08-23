package virtuoel.pehkui.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.client.network.packet.CustomPayloadS2CPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import virtuoel.pehkui.Pehkui;
import virtuoel.pehkui.api.ResizableEntity;
import virtuoel.pehkui.api.ScaleData;

@Mixin(Entity.class)
public abstract class EntityMixin implements ResizableEntity
{
	@Shadow World world;
	@Shadow double x;
	@Shadow double y;
	@Shadow double z;
	@Shadow float stepHeight;
	@Shadow EntityDimensions dimensions;
	
	ScaleData pehkui_scaleData = new ScaleData(Optional.of(((Entity) (Object) this)::calculateDimensions));
	
	@Inject(at = @At("HEAD"), method = "fromTag")
	private void onFromTag(CompoundTag compoundTag_1, CallbackInfo info)
	{
		if(compoundTag_1.containsKey(Pehkui.MOD_ID + ":scale_data", NbtType.COMPOUND))
		{
			pehkui_scaleData.fromTag(compoundTag_1.getCompound(Pehkui.MOD_ID + ":scale_data"));
			
			if(pehkui_scaleData.getScale() != 1.0F && world != null && !world.isClient)
			{
				pehkui_scaleData.markForSync();
			}
		}
	}
	
	@Override
	public ScaleData pehkui_getScaleData()
	{
		return pehkui_scaleData;
	}
	
	@Inject(at = @At("HEAD"), method = "toTag")
	private void onToTag(CompoundTag compoundTag_1, CallbackInfoReturnable<CompoundTag> info)
	{
		compoundTag_1.put(Pehkui.MOD_ID + ":scale_data", pehkui_scaleData.toTag(new CompoundTag()));
	}
	
	@Inject(at = @At("HEAD"), method = "tick")
	private void onTickPre(CallbackInfo info)
	{
		pehkui_scaleData.tick();
	}
	
	@Inject(at = @At("HEAD"), method = "onStartedTrackingBy")
	private void onOnStartedTrackingBy(ServerPlayerEntity serverPlayerEntity_1, CallbackInfo info)
	{
		if(pehkui_scaleData.getScale() != 1.0F)
		{
			serverPlayerEntity_1.networkHandler.sendPacket(new CustomPayloadS2CPacket(Pehkui.SCALE_PACKET, pehkui_scaleData.toPacketByteBuf(new PacketByteBuf(Unpooled.buffer()).writeUuid(((Entity) (Object) this).getUuid()))));
			pehkui_scaleData.scaleModified = false;
		}
	}
	
	@Inject(at = @At("RETURN"), method = "getDimensions", cancellable = true)
	private void onGetDimensions(EntityPose entityPose_1, CallbackInfoReturnable<EntityDimensions> info)
	{
		final float scale = pehkui_scaleData.getScale();
		if(scale != 1.0F)
		{
			info.setReturnValue(info.getReturnValue().scaled(scale));
		}
	}
	
	@Redirect(method = "dropStack(Lnet/minecraft/item/ItemStack;F)Lnet/minecraft/entity/ItemEntity;", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ItemEntity;setToDefaultPickupDelay()V"))
	public void onDropStackSetToDefaultPickupDelayProxy(ItemEntity obj)
	{
		final float scale = pehkui_scaleData.getScale();
		if(scale != 1.0F)
		{
			final ScaleData data = ScaleData.of(obj);
			data.setScale(scale);
			data.setTargetScale(scale);
		}
		obj.setToDefaultPickupDelay();
	}
	
	@Shadow abstract Vec3d clipSneakingMovement(Vec3d vec3d_1, MovementType movementType_1);
	
	@Redirect(method = "move", at = @At(value = "INVOKE", target = "net/minecraft/entity/Entity.clipSneakingMovement(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/entity/MovementType;)Lnet/minecraft/util/math/Vec3d;"))
	public Vec3d onMoveClipSneakingMovementProxy(Entity obj, Vec3d vec3d_1, MovementType movementType_1)
	{
		return clipSneakingMovement(movementType_1 == MovementType.SELF || movementType_1 == MovementType.PLAYER ? vec3d_1.multiply(pehkui_scaleData.getScale()) : vec3d_1, movementType_1);
	}
	
	@Inject(at = @At("HEAD"), method = "spawnSprintingParticles", cancellable = true)
	private void onSpawnSprintingParticles(CallbackInfo info)
	{
		if(pehkui_scaleData.getScale() < 1.0F)
		{
			info.cancel();
		}
	}
	
	@Redirect(method = "clipSneakingMovement", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;stepHeight:F"))
	public float onClipSneakingMovementStepHeightProxy(Entity obj)
	{
		return stepHeight * pehkui_scaleData.getScale();
	}
	
	@Redirect(method = "handleCollisions", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;stepHeight:F"))
	public float onHandleCollisionsStepHeightProxy(Entity obj)
	{
		return stepHeight * pehkui_scaleData.getScale();
	}
	
	@Shadow abstract void setPosition(double double_1, double double_2, double double_3);
	
}
