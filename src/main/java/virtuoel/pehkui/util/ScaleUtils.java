package virtuoel.pehkui.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.util.Identifier;
import virtuoel.pehkui.Pehkui;
import virtuoel.pehkui.api.PehkuiConfig;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleModifier;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.api.ScaleTypes;

public class ScaleUtils
{
	public static void tickScale(ScaleData data)
	{
		final ScaleType type = data.getScaleType();
		
		type.getPreTickEvent().invoker().onEvent(data);
		data.tick();
		type.getPostTickEvent().invoker().onEvent(data);
	}
	
	public static void loadAverageScales(Entity target, Entity source, Entity... sources)
	{
		ScaleData scaleData;
		for (ScaleType type : ScaleRegistries.SCALE_TYPES.values())
		{
			scaleData = type.getScaleData(target);
			
			ScaleData[] scales = new ScaleData[sources.length];
			
			for (int i = 0; i < sources.length; i++)
			{
				scales[i] = type.getScaleData(sources[i]);
			}
			
			scaleData.averagedFromScales(type.getScaleData(source), scales);
		}
	}
	
	public static void loadScaleOnRespawn(Entity target, Entity source, boolean alive)
	{
		if (alive || PehkuiConfig.COMMON.keepAllScalesOnRespawn.get())
		{
			loadScale(target, source);
			return;
		}
		
		final List<? extends String> keptScales = PehkuiConfig.COMMON.scalesKeptOnRespawn.get();
		
		ScaleType type;
		ScaleData sourceData;
		ScaleData targetData;
		SortedSet<ScaleModifier> targetModifiers;
		for (Entry<Identifier, ScaleType> entry : ScaleRegistries.SCALE_TYPES.entrySet())
		{
			type = entry.getValue();
			sourceData = type.getScaleData(source);
			
			if (sourceData.shouldPersist() || keptScales.contains(entry.getKey().toString()))
			{
				targetData = type.getScaleData(target);
				targetData.fromScale(sourceData);
				
				targetModifiers = targetData.getBaseValueModifiers();
				targetModifiers.clear();
				targetModifiers.addAll(sourceData.getBaseValueModifiers());
			}
		}
	}
	
	public static void loadScale(Entity target, Entity source)
	{
		for (ScaleType type : ScaleRegistries.SCALE_TYPES.values())
		{
			type.getScaleData(target).fromScale(type.getScaleData(source));
		}
	}
	
	public static final double DEFAULT_MAXIMUM_REACH_BELOW_1_17 = 32.0D * 16.0D / 4.0D;
	
	public static final float DEFAULT_MINIMUM_POSITIVE_SCALE = 0x1P-96F;
	public static final float DEFAULT_MAXIMUM_POSITIVE_SCALE = 0x1P32F;
	
	private static final float MINIMUM_LIMB_MOTION_SCALE = DEFAULT_MINIMUM_POSITIVE_SCALE;
	
	public static float modifyLimbDistance(float value, Entity entity)
	{
		final float scale = ScaleUtils.getMotionScale(entity);
		
		if (scale == 1.0F)
		{
			return value;
		}
		
		final float ret = value / scale;
		
		if (ret == Float.POSITIVE_INFINITY)
		{
			return MINIMUM_LIMB_MOTION_SCALE;
		}
		else if (ret == Float.NEGATIVE_INFINITY)
		{
			return -MINIMUM_LIMB_MOTION_SCALE;
		}
		else if (ret > MINIMUM_LIMB_MOTION_SCALE || ret < -MINIMUM_LIMB_MOTION_SCALE)
		{
			return ret;
		}
		
		return ret < 0 ? -MINIMUM_LIMB_MOTION_SCALE : MINIMUM_LIMB_MOTION_SCALE;
	}
	
	public static final float modifyProjectionMatrixDepthByWidth(float depth, @Nullable Entity entity, float tickDelta)
	{
		return entity == null ? depth : modifyProjectionMatrixDepth(ScaleUtils.getBoundingBoxWidthScale(entity, tickDelta), depth, entity, tickDelta);
	}
	
	public static final float modifyProjectionMatrixDepthByHeight(float depth, @Nullable Entity entity, float tickDelta)
	{
		return entity == null ? depth : modifyProjectionMatrixDepth(ScaleUtils.getEyeHeightScale(entity, tickDelta), depth, entity, tickDelta);
	}
	
	public static final float modifyProjectionMatrixDepth(float depth, @Nullable Entity entity, float tickDelta)
	{
		return entity == null ? depth : modifyProjectionMatrixDepth(Math.min(ScaleUtils.getBoundingBoxWidthScale(entity, tickDelta), ScaleUtils.getEyeHeightScale(entity, tickDelta)), depth, entity, tickDelta);
	}
	
	public static final float modifyProjectionMatrixDepth(float scale, float depth, Entity entity, float tickDelta)
	{
		if (scale < 1.0F)
		{
			return Math.max(
				(float) PehkuiConfig.CLIENT.minimumCameraDepth.get().doubleValue(),
				depth * scale
			);
		}
		
		return depth;
	}
	
	public static float setScaleOfDrop(Entity entity, Entity source)
	{
		return setScaleOnSpawn(entity, getDropScale(source));
	}
	
	public static float setScaleOfProjectile(Entity entity, Entity source)
	{
		return setScaleOnSpawn(entity, getProjectileScale(source));
	}
	
	public static float setScaleOnSpawn(Entity entity, float scale)
	{
		if (scale != 1.0F)
		{
			ScaleTypes.BASE.getScaleData(entity).setScale(scale);
		}
		
		return scale;
	}
	
	public static NbtCompound buildScaleNbtFromPacketByteBuf(PacketByteBuf buffer)
	{
		final NbtCompound scaleData = new NbtCompound();
		
		final float scale = buffer.readFloat();
		final float prevScale = buffer.readFloat();
		final float fromScale = buffer.readFloat();
		final float toScale = buffer.readFloat();
		final int scaleTicks = buffer.readInt();
		final int totalScaleTicks = buffer.readInt();
		
		scaleData.putFloat("scale", scale);
		scaleData.putFloat("previous", prevScale);
		scaleData.putFloat("initial", fromScale);
		scaleData.putFloat("target", toScale);
		scaleData.putInt("ticks", scaleTicks);
		scaleData.putInt("total_ticks", totalScaleTicks);
		
		final int baseModifierCount = buffer.readInt();
		
		if (baseModifierCount != 0)
		{
			final NbtList modifiers = new NbtList();
			
			for (int i = 0; i < baseModifierCount; i++)
			{
				modifiers.add(NbtOps.INSTANCE.createString(buffer.readString(32767)));
			}
			
			scaleData.put("baseValueModifiers", modifiers);
		}
		
		return scaleData;
	}
	
	public static void syncScalesIfNeeded(Entity entity, Consumer<Packet<?>> packetSender)
	{
		syncScales(entity, packetSender, ScaleData::shouldSync, true);
	}
	
	public static void syncScalesOnTrackingStart(Entity entity, Consumer<Packet<?>> packetSender)
	{
		syncScales(entity, packetSender, ScaleUtils::isScaleDataNotReset, false);
	}
	
	private static boolean isScaleDataNotReset(final ScaleData scaleData)
	{
		return !scaleData.isReset();
	}
	
	private static final ThreadLocal<Collection<ScaleData>> SYNCED_SCALE_DATA = ThreadLocal.withInitial(ArrayList::new);
	
	public static void syncScales(Entity entity, Consumer<Packet<?>> packetSender, Predicate<ScaleData> condition, boolean unmark)
	{
		final Collection<ScaleData> syncedScales = SYNCED_SCALE_DATA.get();
		
		ScaleData scaleData;
		for (final Entry<ScaleType, ScaleData> entry : ((PehkuiEntityExtensions) entity).pehkui_getScales().entrySet())
		{
			scaleData = entry.getValue();
			
			if (condition.test(scaleData))
			{
				syncedScales.add(scaleData);
				
				if (unmark)
				{
					scaleData.markForSync(false);
				}
			}
		}
		
		if (!syncedScales.isEmpty())
		{
			final PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
			
			buffer.writeVarInt(entity.getId());
			buffer.writeInt(syncedScales.size());
			
			for (final ScaleData s : syncedScales)
			{
				buffer.writeIdentifier(ScaleRegistries.getId(ScaleRegistries.SCALE_TYPES, s.getScaleType()));
				s.toPacket(buffer);
			}
			
			packetSender.accept(new CustomPayloadS2CPacket(Pehkui.SCALE_PACKET, buffer));
			syncedScales.clear();
		}
	}
	
	public static float getEyeHeightScale(Entity entity)
	{
		return getEyeHeightScale(entity, 1.0F);
	}
	
	public static float getEyeHeightScale(Entity entity, float tickDelta)
	{
		return getTypedScale(entity, ScaleTypes.EYE_HEIGHT, tickDelta);
	}
	
	public static float getThirdPersonScale(Entity entity, float tickDelta)
	{
		return getTypedScale(entity, ScaleTypes.THIRD_PERSON, tickDelta);
	}
	
	public static float getModelWidthScale(Entity entity)
	{
		return getModelWidthScale(entity, 1.0F);
	}
	
	public static float getModelWidthScale(Entity entity, float tickDelta)
	{
		return getTypedScale(entity, ScaleTypes.MODEL_WIDTH, tickDelta);
	}
	
	public static float getModelHeightScale(Entity entity)
	{
		return getModelHeightScale(entity, 1.0F);
	}
	
	public static float getModelHeightScale(Entity entity, float tickDelta)
	{
		return getTypedScale(entity, ScaleTypes.MODEL_HEIGHT, tickDelta);
	}
	
	public static float getBoundingBoxWidthScale(Entity entity)
	{
		return getBoundingBoxWidthScale(entity, 1.0F);
	}
	
	public static float getBoundingBoxWidthScale(Entity entity, float tickDelta)
	{
		return getTypedScale(entity, ScaleTypes.HITBOX_WIDTH, tickDelta);
	}
	
	public static float getBoundingBoxHeightScale(Entity entity)
	{
		return getBoundingBoxHeightScale(entity, 1.0F);
	}
	
	public static float getBoundingBoxHeightScale(Entity entity, float tickDelta)
	{
		return getTypedScale(entity, ScaleTypes.HITBOX_HEIGHT, tickDelta);
	}
	
	public static float getFallingScale(Entity entity)
	{
		return getFallingScale(entity, 1.0F);
	}
	
	public static float getFallingScale(Entity entity, float tickDelta)
	{
		return getTypedScale(entity, ScaleTypes.FALLING, tickDelta);
	}
	
	public static float getStepHeightScale(Entity entity)
	{
		return getStepHeightScale(entity, 1.0F);
	}
	
	public static float getStepHeightScale(Entity entity, float tickDelta)
	{
		return getTypedScale(entity, ScaleTypes.STEP_HEIGHT, tickDelta);
	}
	
	public static float getViewBobbingScale(Entity entity)
	{
		return getViewBobbingScale(entity, 1.0F);
	}
	
	public static float getViewBobbingScale(Entity entity, float tickDelta)
	{
		return getTypedScale(entity, ScaleTypes.VIEW_BOBBING, tickDelta);
	}
	
	public static float getVisibilityScale(Entity entity)
	{
		return getVisibilityScale(entity, 1.0F);
	}
	
	public static float getVisibilityScale(Entity entity, float tickDelta)
	{
		return getTypedScale(entity, ScaleTypes.VISIBILITY, tickDelta);
	}
	
	public static float getMotionScale(Entity entity)
	{
		return getMotionScale(entity, 1.0F);
	}
	
	public static float getMotionScale(Entity entity, float tickDelta)
	{
		return getConfigurableTypedScale(entity, ScaleTypes.MOTION, PehkuiConfig.COMMON.scaledMotion::get, tickDelta);
	}
	
	public static float getFlightScale(Entity entity)
	{
		return getFlightScale(entity, 1.0F);
	}
	
	public static float getFlightScale(Entity entity, float tickDelta)
	{
		return getTypedScale(entity, ScaleTypes.FLIGHT, tickDelta);
	}
	
	public static float getBlockReachScale(Entity entity)
	{
		return getBlockReachScale(entity, 1.0F);
	}
	
	public static float getBlockReachScale(Entity entity, float tickDelta)
	{
		return getConfigurableTypedScale(entity, ScaleTypes.BLOCK_REACH, PehkuiConfig.COMMON.scaledReach::get, tickDelta);
	}
	
	public static float getEntityReachScale(Entity entity)
	{
		return getEntityReachScale(entity, 1.0F);
	}
	
	public static float getEntityReachScale(Entity entity, float tickDelta)
	{
		return getConfigurableTypedScale(entity, ScaleTypes.ENTITY_REACH, PehkuiConfig.COMMON.scaledReach::get, tickDelta);
	}
	
	public static float getKnockbackScale(Entity entity)
	{
		return getKnockbackScale(entity, 1.0F);
	}
	
	public static float getKnockbackScale(Entity entity, float tickDelta)
	{
		return getTypedScale(entity, ScaleTypes.KNOCKBACK, tickDelta);
	}
	
	public static float getAttackScale(Entity entity)
	{
		return getAttackScale(entity, 1.0F);
	}
	
	public static float getAttackScale(Entity entity, float tickDelta)
	{
		return getConfigurableTypedScale(entity, ScaleTypes.ATTACK, PehkuiConfig.COMMON.scaledAttack::get, tickDelta);
	}
	
	public static float getDefenseScale(Entity entity)
	{
		return getDefenseScale(entity, 1.0F);
	}
	
	public static float getDefenseScale(Entity entity, float tickDelta)
	{
		return getConfigurableTypedScale(entity, ScaleTypes.DEFENSE, PehkuiConfig.COMMON.scaledDefense::get, tickDelta);
	}
	
	public static float getHealthScale(Entity entity)
	{
		return getHealthScale(entity, 1.0F);
	}
	
	public static float getHealthScale(Entity entity, float tickDelta)
	{
		return getConfigurableTypedScale(entity, ScaleTypes.HEALTH, PehkuiConfig.COMMON.scaledHealth::get, tickDelta);
	}
	
	public static float getDropScale(Entity entity)
	{
		return getDropScale(entity, 1.0F);
	}
	
	public static float getDropScale(Entity entity, float tickDelta)
	{
		return getConfigurableTypedScale(entity, ScaleTypes.DROPS, PehkuiConfig.COMMON.scaledItemDrops::get, tickDelta);
	}
	
	public static float getHeldItemScale(Entity entity)
	{
		return getHeldItemScale(entity, 1.0F);
	}
	
	public static float getHeldItemScale(Entity entity, float tickDelta)
	{
		return getConfigurableTypedScale(entity, ScaleTypes.HELD_ITEM, Boolean.TRUE::booleanValue, tickDelta);
	}
	
	public static float getProjectileScale(Entity entity)
	{
		return getProjectileScale(entity, 1.0F);
	}
	
	public static float getProjectileScale(Entity entity, float tickDelta)
	{
		return getConfigurableTypedScale(entity, ScaleTypes.PROJECTILES, PehkuiConfig.COMMON.scaledProjectiles::get, tickDelta);
	}
	
	public static float getExplosionScale(Entity entity)
	{
		return getExplosionScale(entity, 1.0F);
	}
	
	public static float getExplosionScale(Entity entity, float tickDelta)
	{
		return getConfigurableTypedScale(entity, ScaleTypes.EXPLOSIONS, PehkuiConfig.COMMON.scaledExplosions::get, tickDelta);
	}
	
	public static float getConfigurableTypedScale(Entity entity, ScaleType type, Supplier<Boolean> config, float tickDelta)
	{
		return config.get() ? getTypedScale(entity, type, tickDelta) : type.getDefaultBaseScale();
	}
	
	public static float getTypedScale(Entity entity, ScaleType type, float tickDelta)
	{
		return entity == null ? type.getDefaultBaseScale() : type.getScaleData(entity).getScale(tickDelta);
	}
}
