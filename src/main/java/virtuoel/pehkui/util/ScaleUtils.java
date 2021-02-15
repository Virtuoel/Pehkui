package virtuoel.pehkui.util;

import java.util.Optional;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.util.Identifier;
import virtuoel.pehkui.Pehkui;
import virtuoel.pehkui.api.PehkuiConfig;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.entity.ResizableEntity;

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
	
	public static void loadScale(Object target, Object source)
	{
		ScaleData scaleData;
		for (ScaleType type : ScaleRegistries.SCALE_TYPES.values())
		{
			scaleData = type.getScaleData((Entity) target);
			scaleData.fromScale(type.getScaleData((Entity) source));
		}
	}
	
	public static void setScale(Object entity, float scale)
	{
		if (scale != 1.0F)
		{
			ScaleType.BASE.getScaleData((Entity) entity).setScale(scale);
		}
	}
	
	public static void syncScalesIfNeeded(Entity entity, Consumer<Packet<?>> packetSender)
	{
		syncScales(entity, packetSender, ScaleData::shouldSync, true);
	}
	
	public static void syncScalesOnTrackingStart(Entity entity, Consumer<Packet<?>> packetSender)
	{
		syncScales(entity, packetSender, s -> !s.equals(ScaleData.IDENTITY), false);
	}
	
	public static void syncScales(Entity entity, Consumer<Packet<?>> packetSender, Predicate<ScaleData> condition, boolean unmark)
	{
		final UUID uuid = entity.getUuid();
		
		ScaleData scaleData;
		for (Entry<Identifier, ScaleType> entry : ScaleRegistries.SCALE_TYPES.entrySet())
		{
			scaleData = entry.getValue().getScaleData(entity);
			
			if (condition.test(scaleData))
			{
				packetSender.accept(new CustomPayloadS2CPacket(Pehkui.SCALE_PACKET,
					scaleData.toPacketByteBuf(
						new PacketByteBuf(Unpooled.buffer())
						.writeUuid(uuid)
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
	
	public static float getWidthScale(Object entity)
	{
		return getWidthScale(entity, 1.0F);
	}
	
	public static float getWidthScale(Object entity, float tickDelta)
	{
		return getTypedScale(entity, ScaleType.WIDTH, tickDelta);
	}
	
	public static float getHeightScale(Object entity)
	{
		return getHeightScale(entity, 1.0F);
	}
	
	public static float getHeightScale(Object entity, float tickDelta)
	{
		return getTypedScale(entity, ScaleType.HEIGHT, tickDelta);
	}
	
	public static float getMotionScale(Object entity)
	{
		return getMotionScale(entity, 1.0F);
	}
	
	public static float getMotionScale(Object entity, float tickDelta)
	{
		return getConfigurableTypedScale(entity, ScaleType.MOTION, "scaledMotion", tickDelta);
	}
	
	public static float getReachScale(Object entity)
	{
		return getReachScale(entity, 1.0F);
	}
	
	public static float getReachScale(Object entity, float tickDelta)
	{
		return getConfigurableTypedScale(entity, ScaleType.REACH, "scaledReach", tickDelta);
	}
	
	public static float getDropScale(Object entity)
	{
		return getDropScale(entity, 1.0F);
	}
	
	public static float getDropScale(Object entity, float tickDelta)
	{
		return getConfigurableTypedScale(entity, ScaleType.DROPS, "scaledItemDrops", tickDelta);
	}
	
	public static float getProjectileScale(Object entity)
	{
		return getProjectileScale(entity, 1.0F);
	}
	
	public static float getProjectileScale(Object entity, float tickDelta)
	{
		return getConfigurableTypedScale(entity, ScaleType.PROJECTILES, "scaledProjectiles", tickDelta);
	}
	
	public static float getExplosionScale(Object entity)
	{
		return getExplosionScale(entity, 1.0F);
	}
	
	public static float getExplosionScale(Object entity, float tickDelta)
	{
		return getConfigurableTypedScale(entity, ScaleType.EXPLOSIONS, "scaledExplosions", tickDelta);
	}
	
	public static float getConfigurableTypedScale(Object entity, ScaleType type, String config, float tickDelta)
	{
		if (Optional.ofNullable(PehkuiConfig.DATA.get(config))
			.filter(JsonElement::isJsonPrimitive).map(JsonElement::getAsJsonPrimitive)
			.filter(JsonPrimitive::isBoolean).map(JsonPrimitive::getAsBoolean)
			.orElse(true))
		{
			return getTypedScale(entity, type, tickDelta);
		}
		
		return getTypedScale(entity, ScaleType.BASE, tickDelta);
	}
	
	public static float getTypedScale(Object entity, ScaleType type, float tickDelta)
	{
		if (!(entity instanceof ResizableEntity))
		{
			return type.getDefaultBaseScale();
		}
		
		return type.getScaleData((Entity) entity).getScale(tickDelta);
	}
}
