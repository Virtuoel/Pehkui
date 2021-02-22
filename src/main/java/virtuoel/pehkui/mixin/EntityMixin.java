package virtuoel.pehkui.mixin;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import virtuoel.pehkui.Pehkui;
import virtuoel.pehkui.api.PehkuiConfig;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.entity.ResizableEntity;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(Entity.class)
public abstract class EntityMixin implements ResizableEntity
{
	@Shadow World world;
	
	private final Map<ScaleType, ScaleData> pehkui_scaleTypes = new Object2ObjectOpenHashMap<>();
	
	@Override
	public ScaleData pehkui_getScaleData(ScaleType type)
	{
		ScaleData scaleData = pehkui_scaleTypes.get(type);
		
		if (!pehkui_scaleTypes.containsKey(type))
		{
			pehkui_scaleTypes.put(type, scaleData = pehkui_constructScaleData(type));
		}
		
		return scaleData;
	}
	
	@Deprecated
	@Inject(at = @At("HEAD"), method = "readNbt")
	private void onReadNbtLegacy(CompoundTag tag, CallbackInfo info)
	{
		if (tag.contains(Pehkui.MOD_ID + ":scale_data", NbtType.COMPOUND))
		{
			final ScaleData scaleData = pehkui_getScaleData(ScaleType.BASE);
			scaleData.fromTag(tag.getCompound(Pehkui.MOD_ID + ":scale_data"));
		}
	}
	
	@Inject(at = @At("HEAD"), method = "readNbt")
	private void onReadNbt(CompoundTag tag, CallbackInfo info)
	{
		if (tag.contains(Pehkui.MOD_ID + ":scale_data_types", NbtType.COMPOUND))
		{
			final CompoundTag typeData = tag.getCompound(Pehkui.MOD_ID + ":scale_data_types");
			
			String key;
			ScaleData scaleData;
			for (Entry<Identifier, ScaleType> entry : ScaleRegistries.SCALE_TYPES.entrySet())
			{
				key = entry.getKey().toString();
				
				if (typeData.contains(key, NbtType.COMPOUND))
				{
					scaleData = pehkui_getScaleData(entry.getValue());
					scaleData.fromTag(typeData.getCompound(key));
				}
			}
		}
	}
	
	@Inject(at = @At("HEAD"), method = "writeNbt")
	private void onWriteNbt(CompoundTag tag, CallbackInfoReturnable<CompoundTag> info)
	{
		final CompoundTag typeData = new CompoundTag();
		
		ScaleData scaleData;
		CompoundTag compound;
		for (Entry<Identifier, ScaleType> entry : ScaleRegistries.SCALE_TYPES.entrySet())
		{
			scaleData = pehkui_getScaleData(entry.getValue());
			
			if (!scaleData.equals(ScaleData.IDENTITY))
			{
				compound = new CompoundTag();
				
				scaleData.toTag(compound);
				
				if (!compound.isEmpty())
				{
					typeData.put(entry.getKey().toString(), compound);
				}
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
		for (ScaleType type : ScaleRegistries.SCALE_TYPES.values())
		{
			ScaleUtils.tickScale(pehkui_getScaleData(type));
		}
	}
	
	@Inject(at = @At("RETURN"), method = "getDimensions", cancellable = true)
	private void onGetDimensions(EntityPose pose, CallbackInfoReturnable<EntityDimensions> info)
	{
		final float widthScale = ScaleUtils.getWidthScale((Entity) (Object) this);
		final float heightScale = ScaleUtils.getHeightScale((Entity) (Object) this);
		
		if (widthScale != 1.0F || heightScale != 1.0F)
		{
			info.setReturnValue(info.getReturnValue().scaled(widthScale, heightScale));
		}
	}
	
	@Inject(method = "dropStack(Lnet/minecraft/item/ItemStack;F)Lnet/minecraft/entity/ItemEntity;", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ItemEntity;setToDefaultPickupDelay()V"))
	private void onDropStack(ItemStack stack, float yOffset, CallbackInfoReturnable<ItemEntity> info, ItemEntity entity)
	{
		ScaleUtils.setScaleOfDrop(entity, (Entity) (Object) this);
	}
	
	@ModifyArg(method = "fall", index = 3, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onLandedUpon(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;F)V"))
	private float onFallModifyFallDistance(float distance)
	{
		final float scale = ScaleUtils.getMotionScale((Entity) (Object) this);
		
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
		final float scale = ScaleUtils.getMotionScale((Entity) (Object) this);
		
		return scale < 1.0F ? scale * scale * value : value;
	}
	
	@ModifyArg(method = "move", index = 0, at = @At(value = "INVOKE", target = "net/minecraft/entity/Entity.adjustMovementForSneaking(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/entity/MovementType;)Lnet/minecraft/util/math/Vec3d;"))
	private Vec3d onMoveAdjustMovementForSneakingProxy(Vec3d movement, MovementType type)
	{
		if (type == MovementType.SELF || type == MovementType.PLAYER)
		{
			return movement.multiply(ScaleUtils.getMotionScale((Entity) (Object) this));
		}
		
		return movement;
	}
	
	@Inject(at = @At("HEAD"), method = "spawnSprintingParticles", cancellable = true)
	private void onSpawnSprintingParticles(CallbackInfo info)
	{
		if (ScaleUtils.getMotionScale((Entity) (Object) this) < 1.0F)
		{
			info.cancel();
		}
	}
}
