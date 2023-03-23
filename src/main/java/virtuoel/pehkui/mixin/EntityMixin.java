package virtuoel.pehkui.mixin;

import java.util.Map;
import java.util.Map.Entry;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.util.Constants;
import virtuoel.pehkui.Pehkui;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.server.command.DebugCommand;
import virtuoel.pehkui.util.PehkuiEntityExtensions;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(Entity.class)
public abstract class EntityMixin implements PehkuiEntityExtensions
{
	@Shadow boolean onGround;
	@Shadow boolean firstUpdate;
	
	private volatile Map<ScaleType, ScaleData> pehkui_scaleTypes = Object2ObjectMaps.synchronize(new Object2ObjectOpenHashMap<>());
	private boolean pehkui_shouldSyncScales = false;
	private boolean pehkui_shouldIgnoreScaleNbt = false;
	
	@Override
	public ScaleData pehkui_constructScaleData(ScaleType type)
	{
		return ScaleData.Builder.create().type(type).entity((Entity) (Object) this).build();
	}
	
	@Override
	public ScaleData pehkui_getScaleData(ScaleType type)
	{
		final Map<ScaleType, ScaleData> scaleTypes = pehkui_getScales();
		
		ScaleData scaleData = scaleTypes.get(type);
		
		if (scaleData == null)
		{
			synchronized (scaleTypes)
			{
				if (!scaleTypes.containsKey(type))
				{
					scaleTypes.put(type, null);
					scaleTypes.put(type, scaleData = pehkui_constructScaleData(type));
				}
				else
				{
					scaleData = scaleTypes.get(type);
				}
			}
		}
		
		return scaleData;
	}
	
	@Override
	public Map<ScaleType, ScaleData> pehkui_getScales()
	{
		Map<ScaleType, ScaleData> scaleTypes = pehkui_scaleTypes;
		
		if (scaleTypes == null)
		{
			synchronized (this)
			{
				scaleTypes = pehkui_scaleTypes;
				
				if (scaleTypes == null)
				{
					pehkui_scaleTypes = scaleTypes = Object2ObjectMaps.synchronize(new Object2ObjectOpenHashMap<>());
				}
			}
		}
		
		return scaleTypes;
	}
	
	@Override
	public void pehkui_setShouldSyncScales(boolean sync)
	{
		pehkui_shouldSyncScales = sync;
	}
	
	@Override
	public boolean pehkui_shouldSyncScales()
	{
		return pehkui_shouldSyncScales;
	}
	
	@Override
	public boolean pehkui_shouldIgnoreScaleNbt()
	{
		return pehkui_shouldIgnoreScaleNbt;
	}
	
	@Override
	public void pehkui_setShouldIgnoreScaleNbt(boolean ignore)
	{
		pehkui_shouldIgnoreScaleNbt = ignore;
	}
	
	@Inject(at = @At("HEAD"), method = "readNbt")
	private void pehkui$readNbt(NbtCompound tag, CallbackInfo info)
	{
		pehkui_readScaleNbt(tag);
	}
	
	@Override
	public void pehkui_readScaleNbt(NbtCompound nbt)
	{
		if (pehkui_shouldIgnoreScaleNbt())
		{
			return;
		}
		
		if (nbt.contains(Pehkui.MOD_ID + ":scale_data_types", Constants.NBT.TAG_COMPOUND) && !DebugCommand.unmarkEntityForScaleReset((Entity) (Object) this, nbt))
		{
			final NbtCompound typeData = nbt.getCompound(Pehkui.MOD_ID + ":scale_data_types");
			
			String key;
			ScaleData scaleData;
			for (Entry<Identifier, ScaleType> entry : ScaleRegistries.SCALE_TYPES.entrySet())
			{
				key = entry.getKey().toString();
				
				if (typeData.contains(key, Constants.NBT.TAG_COMPOUND))
				{
					scaleData = pehkui_getScaleData(entry.getValue());
					scaleData.readNbt(typeData.getCompound(key));
				}
			}
		}
	}
	
	@Inject(at = @At("HEAD"), method = "writeNbt")
	private void pehkui$writeNbt(NbtCompound tag, CallbackInfoReturnable<NbtCompound> info)
	{
		pehkui_writeScaleNbt(tag);
	}
	
	@Override
	public NbtCompound pehkui_writeScaleNbt(NbtCompound nbt)
	{
		if (pehkui_shouldIgnoreScaleNbt())
		{
			return nbt;
		}
		
		final NbtCompound typeData = new NbtCompound();
		
		NbtCompound compound;
		for (final Entry<ScaleType, ScaleData> entry : pehkui_getScales().entrySet())
		{
			compound = entry.getValue().writeNbt(new NbtCompound());
			
			if (compound.getSize() != 0)
			{
				typeData.put(ScaleRegistries.getId(ScaleRegistries.SCALE_TYPES, entry.getKey()).toString(), compound);
			}
		}
		
		if (typeData.getSize() > 0)
		{
			nbt.put(Pehkui.MOD_ID + ":scale_data_types", typeData);
		}
		
		return nbt;
	}
	
	@Inject(at = @At("HEAD"), method = "tick")
	private void pehkui$tick(CallbackInfo info)
	{
		for (ScaleType type : ScaleRegistries.SCALE_TYPES.values())
		{
			ScaleUtils.tickScale(pehkui_getScaleData(type));
		}
	}
	
	@Inject(at = @At("RETURN"), method = "getDimensions", cancellable = true)
	private void pehkui$getDimensions(EntityPose pose, CallbackInfoReturnable<EntityDimensions> info)
	{
		final float widthScale = ScaleUtils.getBoundingBoxWidthScale((Entity) (Object) this);
		final float heightScale = ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this);
		
		if (widthScale != 1.0F || heightScale != 1.0F)
		{
			info.setReturnValue(info.getReturnValue().scaled(widthScale, heightScale));
		}
	}
	
	@Inject(at = @At("HEAD"), method = "onStartedTrackingBy")
	private void pehkui$onStartedTrackingBy(ServerPlayerEntity player, CallbackInfo info)
	{
		ScaleUtils.syncScalesOnTrackingStart((Entity) (Object) this, player.networkHandler::sendPacket);
	}
	
	@ModifyVariable(method = "dropStack(Lnet/minecraft/item/ItemStack;F)Lnet/minecraft/entity/ItemEntity;", at = @At(value = "STORE"))
	private ItemEntity pehkui$dropStack(ItemEntity entity)
	{
		ScaleUtils.setScaleOfDrop(entity, (Entity) (Object) this);
		return entity;
	}
	
	@ModifyConstant(method = "move", constant = @Constant(doubleValue = 1.0E-7D))
	private double pehkui$move$minVelocity(double value)
	{
		final float scale = ScaleUtils.getMotionScale((Entity) (Object) this);
		
		return scale < 1.0F ? scale * scale * value : value;
	}
	
	@ModifyArg(method = "move", index = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;adjustMovementForSneaking(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/entity/MovementType;)Lnet/minecraft/util/math/Vec3d;"))
	private Vec3d pehkui$move$adjustMovementForSneaking(Vec3d movement, MovementType type)
	{
		if (type == MovementType.SELF || type == MovementType.PLAYER)
		{
			return movement.multiply(ScaleUtils.getMotionScale((Entity) (Object) this));
		}
		
		return movement;
	}
	
	@Unique private static final ThreadLocal<Entity> pehkui$COLLIDING = new ThreadLocal<>();
	
	@Inject(at = @At("HEAD"), method = "pushAwayFrom")
	private void pehkui$pushAwayFrom(Entity other, CallbackInfo info)
	{
		pehkui$COLLIDING.set(other);
	}
	
	@ModifyArg(method = "pushAwayFrom", index = 0, at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/entity/Entity;addVelocity(DDD)V"))
	private double pehkui$pushOtherAwayFrom$ownX(double value)
	{
		return value * ScaleUtils.getMotionScale(pehkui$COLLIDING.get());
	}
	
	@ModifyArg(method = "pushAwayFrom", index = 2, at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/entity/Entity;addVelocity(DDD)V"))
	private double pehkui$pushOtherAwayFrom$ownZ(double value)
	{
		return value * ScaleUtils.getMotionScale(pehkui$COLLIDING.get());
	}
	
	@ModifyArg(method = "pushAwayFrom", index = 0, at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/entity/Entity;addVelocity(DDD)V"))
	private double pehkui$pushOtherAwayFrom$otherX(double value)
	{
		return value * ScaleUtils.getMotionScale((Entity) (Object) this);
	}
	
	@ModifyArg(method = "pushAwayFrom", index = 2, at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/entity/Entity;addVelocity(DDD)V"))
	private double pehkui$pushOtherAwayFrom$otherZ(double value)
	{
		return value * ScaleUtils.getMotionScale((Entity) (Object) this);
	}
	
	@Inject(at = @At("HEAD"), method = "spawnSprintingParticles", cancellable = true)
	private void pehkui$spawnSprintingParticles(CallbackInfo info)
	{
		if (ScaleUtils.getMotionScale((Entity) (Object) this) < 1.0F)
		{
			info.cancel();
		}
	}
	
	@Override
	public boolean pehkui_isFirstUpdate()
	{
		return this.firstUpdate;
	}
	
	@Override
	public boolean pehkui_getOnGround()
	{
		return this.onGround;
	}
	
	@Override
	public void pehkui_setOnGround(boolean onGround)
	{
		this.onGround = onGround;
	}
}
