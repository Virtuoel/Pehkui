package virtuoel.pehkui.util;

import java.util.List;
import java.util.Map.Entry;
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
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.api.ScaleType;

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
		for (Entry<Identifier, ScaleType> entry : ScaleRegistries.SCALE_TYPES.entrySet())
		{
			if (keptScales.contains(entry.getKey().toString()))
			{
				type = entry.getValue();
				type.getScaleData(target).fromScale(type.getScaleData(source));
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
	
	public static boolean isAboveCollisionThreshold(Entity entity)
	{
		final float widthScale = ScaleUtils.getWidthScale(entity);
		final float heightScale = ScaleUtils.getHeightScale(entity);
		final double volume = widthScale * widthScale * heightScale;
		
		final double scaleThreshold = PehkuiConfig.COMMON.largeScaleCollisionThreshold.get();
		final double threshold = scaleThreshold * scaleThreshold * scaleThreshold;
		
		return volume > threshold;
	}
	
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
		
		if (ret > MINIMUM_LIMB_MOTION_SCALE || ret < -MINIMUM_LIMB_MOTION_SCALE)
		{
			return ret;
		}
		
		return ret < 0 ? -MINIMUM_LIMB_MOTION_SCALE : MINIMUM_LIMB_MOTION_SCALE;
	}
	
	public static final float modifyProjectionMatrixDepthByWidth(float depth, @Nullable Entity entity, float tickDelta)
	{
		return entity == null ? depth : modifyProjectionMatrixDepth(ScaleUtils.getWidthScale(entity, tickDelta), depth, entity, tickDelta);
	}
	
	public static final float modifyProjectionMatrixDepthByHeight(float depth, @Nullable Entity entity, float tickDelta)
	{
		return entity == null ? depth : modifyProjectionMatrixDepth(ScaleUtils.getHeightScale(entity, tickDelta), depth, entity, tickDelta);
	}
	
	public static final float modifyProjectionMatrixDepth(float depth, @Nullable Entity entity, float tickDelta)
	{
		return entity == null ? depth : modifyProjectionMatrixDepth(Math.min(ScaleUtils.getWidthScale(entity, tickDelta), ScaleUtils.getHeightScale(entity, tickDelta)), depth, entity, tickDelta);
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
			ScaleType.BASE.getScaleData(entity).setScale(scale);
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
		syncScales(entity, packetSender, s -> !s.isReset(), false);
	}
	
	public static void syncScales(Entity entity, Consumer<Packet<?>> packetSender, Predicate<ScaleData> condition, boolean unmark)
	{
		final int id = entity.getId();
		
		ScaleData scaleData;
		for (Entry<Identifier, ScaleType> entry : ScaleRegistries.SCALE_TYPES.entrySet())
		{
			scaleData = entry.getValue().getScaleData(entity);
			
			if (condition.test(scaleData))
			{
				packetSender.accept(new CustomPayloadS2CPacket(Pehkui.SCALE_PACKET,
					scaleData.toPacket(
						new PacketByteBuf(Unpooled.buffer())
						.writeVarInt(id)
						.writeIdentifier(entry.getKey())
					)
				));
				
				if (unmark)
				{
					scaleData.markForSync(false);
				}
			}
		}
	}
	
	public static float getWidthScale(Entity entity)
	{
		return getWidthScale(entity, 1.0F);
	}
	
	public static float getWidthScale(Entity entity, float tickDelta)
	{
		return getTypedScale(entity, ScaleType.WIDTH, tickDelta);
	}
	
	public static float getHeightScale(Entity entity)
	{
		return getHeightScale(entity, 1.0F);
	}
	
	public static float getHeightScale(Entity entity, float tickDelta)
	{
		return getTypedScale(entity, ScaleType.HEIGHT, tickDelta);
	}
	
	public static float getMotionScale(Entity entity)
	{
		return getMotionScale(entity, 1.0F);
	}
	
	public static float getMotionScale(Entity entity, float tickDelta)
	{
		return getConfigurableTypedScale(entity, ScaleType.MOTION, PehkuiConfig.COMMON.scaledMotion::get, tickDelta);
	}
	
	public static float getReachScale(Entity entity)
	{
		return getReachScale(entity, 1.0F);
	}
	
	public static float getReachScale(Entity entity, float tickDelta)
	{
		return getConfigurableTypedScale(entity, ScaleType.REACH, PehkuiConfig.COMMON.scaledReach::get, tickDelta);
	}
	
	public static float getAttackScale(Entity entity)
	{
		return getAttackScale(entity, 1.0F);
	}
	
	public static float getAttackScale(Entity entity, float tickDelta)
	{
		return getConfigurableTypedScale(entity, ScaleType.ATTACK, PehkuiConfig.COMMON.scaledAttack::get, tickDelta);
	}
	
	public static float getDefenseScale(Entity entity)
	{
		return getDefenseScale(entity, 1.0F);
	}
	
	public static float getDefenseScale(Entity entity, float tickDelta)
	{
		return getConfigurableTypedScale(entity, ScaleType.DEFENSE, PehkuiConfig.COMMON.scaledDefense::get, tickDelta);
	}
	
	public static float getHealthScale(Entity entity)
	{
		return getHealthScale(entity, 1.0F);
	}
	
	public static float getHealthScale(Entity entity, float tickDelta)
	{
		return getConfigurableTypedScale(entity, ScaleType.HEALTH, PehkuiConfig.COMMON.scaledHealth::get, tickDelta);
	}
	
	public static float getDropScale(Entity entity)
	{
		return getDropScale(entity, 1.0F);
	}
	
	public static float getDropScale(Entity entity, float tickDelta)
	{
		return getConfigurableTypedScale(entity, ScaleType.DROPS, PehkuiConfig.COMMON.scaledItemDrops::get, tickDelta);
	}
	
	public static float getProjectileScale(Entity entity)
	{
		return getProjectileScale(entity, 1.0F);
	}
	
	public static float getProjectileScale(Entity entity, float tickDelta)
	{
		return getConfigurableTypedScale(entity, ScaleType.PROJECTILES, PehkuiConfig.COMMON.scaledProjectiles::get, tickDelta);
	}
	
	public static float getExplosionScale(Entity entity)
	{
		return getExplosionScale(entity, 1.0F);
	}
	
	public static float getExplosionScale(Entity entity, float tickDelta)
	{
		return getConfigurableTypedScale(entity, ScaleType.EXPLOSIONS, PehkuiConfig.COMMON.scaledExplosions::get, tickDelta);
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
