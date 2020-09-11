package virtuoel.pehkui.mixin;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import virtuoel.pehkui.Pehkui;
import virtuoel.pehkui.api.PehkuiConfig;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.entity.ResizableEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(Entity.class)
public abstract class EntityMixin implements ResizableEntity
{
	@Shadow World world;
	
	@Shadow abstract void updatePosition(double x, double y, double z);
	
	private final Map<ScaleType, ScaleData> pehkui_scaleTypes = new Object2ObjectOpenHashMap<>();
	
	@Override
	public ScaleData pehkui_getScaleData(ScaleType type)
	{
		if (!pehkui_scaleTypes.containsKey(type))
		{
			pehkui_scaleTypes.put(type, pehkui_constructScaleData(type));
		}
		
		return pehkui_scaleTypes.get(type);
	}
	
	@Inject(at = @At("HEAD"), method = "fromTag")
	private void onFromTag(CompoundTag tag, CallbackInfo info)
	{
		final boolean isServerWorld = world != null && !world.isClient;
		
		if (tag.contains(Pehkui.MOD_ID + ":scale_data", NbtType.COMPOUND))
		{
			final ScaleData scaleData = pehkui_getScaleData(ScaleType.BASE);
			scaleData.fromTag(tag.getCompound(Pehkui.MOD_ID + ":scale_data"));
			
			if (scaleData.getScale() != 1.0F && isServerWorld)
			{
				scaleData.markForSync();
			}
		}
		
		if (tag.contains(Pehkui.MOD_ID + ":scale_data_types", NbtType.COMPOUND))
		{
			final CompoundTag typeData = tag.getCompound(Pehkui.MOD_ID + ":scale_data_types");
			
			String key;
			ScaleData scaleData;
			for (Entry<Identifier, ScaleType> entry : ScaleType.REGISTRY.entrySet())
			{
				key = entry.getKey().toString();
				
				if (typeData.contains(key, NbtType.COMPOUND))
				{
					scaleData = pehkui_getScaleData(entry.getValue());
					scaleData.fromTag(typeData.getCompound(key));
					
					if (scaleData.getScale() != 1.0F && isServerWorld)
					{
						scaleData.markForSync();
					}
				}
			}
		}
	}
	
	@Inject(at = @At("HEAD"), method = "toTag")
	private void onToTag(CompoundTag tag, CallbackInfoReturnable<CompoundTag> info)
	{
		final CompoundTag typeData = new CompoundTag();
		
		ScaleData scaleData;
		for (Entry<Identifier, ScaleType> entry : ScaleType.REGISTRY.entrySet())
		{
			scaleData = pehkui_getScaleData(entry.getValue());
			
			if (!scaleData.equals(ScaleData.IDENTITY))
			{
				typeData.put(entry.getKey().toString(), scaleData.toTag(new CompoundTag()));
			}
		}
		
		if (typeData.getSize() > 0)
		{
			tag.put(Pehkui.MOD_ID + ":scale_data_types", typeData);
		}
	}
	
	@Inject(at = @At("HEAD"), method = "tick")
	private void onTickPre(CallbackInfo info)
	{
		for (ScaleType type : ScaleType.REGISTRY.values())
		{
			pehkui_getScaleData(type).tick();
		}
	}
	
	@Inject(at = @At("HEAD"), method = "onStartedTrackingBy")
	private void onOnStartedTrackingBy(ServerPlayerEntity player, CallbackInfo info)
	{
		ScaleData scaleData;
		for (Entry<Identifier, ScaleType> entry : ScaleType.REGISTRY.entrySet())
		{
			scaleData = pehkui_getScaleData(entry.getValue());
			
			if (scaleData.getScale() != 1.0F)
			{
				player.networkHandler.sendPacket(new CustomPayloadS2CPacket(Pehkui.SCALE_PACKET,
					scaleData.toPacketByteBuf(
						new PacketByteBuf(Unpooled.buffer())
						.writeUuid(((Entity) (Object) this).getUuid())
						.writeIdentifier(entry.getKey())
					)
				));
				scaleData.scaleModified = false;
			}
		}
	}
	
	@Inject(at = @At("RETURN"), method = "getDimensions", cancellable = true)
	private void onGetDimensions(EntityPose pose, CallbackInfoReturnable<EntityDimensions> info)
	{
		final float widthScale = ScaleUtils.getWidthScale(this);
		final float heightScale = ScaleUtils.getHeightScale(this);
		
		if (widthScale != 1.0F || heightScale != 1.0F)
		{
			info.setReturnValue(info.getReturnValue().scaled(widthScale, heightScale));
		}
	}
	
	@Inject(method = "dropStack(Lnet/minecraft/item/ItemStack;F)Lnet/minecraft/entity/ItemEntity;", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ItemEntity;setToDefaultPickupDelay()V"))
	private void onDropStack(ItemStack stack, float yOffset, CallbackInfoReturnable<ItemEntity> info, ItemEntity entity)
	{
		ScaleUtils.setScale(entity, ScaleUtils.getDropScale(this));
	}
	
	@ModifyArg(method = "fall", index = 3, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onLandedUpon(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;F)V"))
	private float onFallModifyFallDistance(float distance)
	{
		final float scale = ScaleUtils.getMotionScale(this);
		
		if (scale != 1.0F)
		{
			if (Optional.ofNullable(PehkuiConfig.DATA.get("scaledFallDamage"))
				.filter(JsonElement::isJsonPrimitive).map(JsonElement::getAsJsonPrimitive)
				.filter(JsonPrimitive::isBoolean).map(JsonPrimitive::getAsBoolean)
				.orElse(true))
			{
				return distance / scale;
			}
		}
		
		return distance;
	}
	
	@ModifyConstant(method = "move", constant = @Constant(doubleValue = 1.0E-7D))
	private double moveModifyMinVelocity(double value)
	{
		final float scale = ScaleUtils.getMotionScale(this);
		
		return scale < 1.0F ? scale * scale * value : value;
	}
	
	@ModifyArg(method = "move", index = 0, at = @At(value = "INVOKE", target = "net/minecraft/entity/Entity.adjustMovementForSneaking(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/entity/MovementType;)Lnet/minecraft/util/math/Vec3d;"))
	private Vec3d onMoveAdjustMovementForSneakingProxy(Vec3d movement, MovementType type)
	{
		if (type == MovementType.SELF || type == MovementType.PLAYER)
		{
			return movement.multiply(ScaleUtils.getMotionScale(this));
		}
		
		return movement;
	}
	
	@Inject(at = @At("HEAD"), method = "spawnSprintingParticles", cancellable = true)
	private void onSpawnSprintingParticles(CallbackInfo info)
	{
		if (ScaleUtils.getMotionScale(this) < 1.0F)
		{
			info.cancel();
		}
	}
	
	@Shadow @Final @Mutable EntityType<?> type;
	@Shadow abstract void move(MovementType type, Vec3d movement);
	
	@Inject(method = "calculateDimensions", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", shift = Shift.AFTER, ordinal = 1, target = "Lnet/minecraft/entity/Entity;setBoundingBox(Lnet/minecraft/util/math/Box;)V"))
	private void calculateDimensionsMoveProxy(CallbackInfo info, EntityDimensions previous, EntityPose pose, EntityDimensions current, Box box)
	{
		if (this.world.isClient && type == EntityType.PLAYER && current.width > previous.width)
		{
			final float scale = ScaleUtils.getWidthScale(this);
			final float dist = (previous.width - current.width) / 2.0F;
			
			move(MovementType.SELF, new Vec3d(dist / scale, 0.0D, dist / scale));
		}
	}
}
