package virtuoel.pehkui.util;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.jetbrains.annotations.Nullable;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.math.Box;
import virtuoel.pehkui.Pehkui;
import virtuoel.pehkui.api.PehkuiConfig;

public class ScaleRenderUtils
{
	public static final MethodHandle DRAW_BOX_OUTLINE, SHOULD_KEEP_PLAYER_ATTRIBUTES;
	
	static
	{
		final MappingResolver mappingResolver = FabricLoader.getInstance().getMappingResolver();
		final Int2ObjectMap<MethodHandle> h = new Int2ObjectArrayMap<MethodHandle>();
		
		final Lookup lookup = MethodHandles.lookup();
		String mapped = "unset";
		Method m;
		
		try
		{
			final boolean is114Minus = VersionUtils.MINOR <= 14;
			final boolean is116Plus = VersionUtils.MINOR >= 16;
			final boolean is1192Minus = VersionUtils.MINOR < 19 || (VersionUtils.MINOR == 19 && VersionUtils.PATCH <= 2);
			
			if (is114Minus && FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
			{
				mapped = mappingResolver.mapMethodName("intermediary", "net.minecraft.class_761", "method_3260", "(Lnet/minecraft/class_238;FFFF)V");
				m = WorldRenderer.class.getMethod(mapped, Box.class, float.class, float.class, float.class, float.class);
				h.put(0, lookup.unreflect(m));
			}
			
			if (is116Plus && is1192Minus)
			{
				mapped = mappingResolver.mapMethodName("intermediary", "net.minecraft.class_2724", "method_27904", "()Z");
				m = PlayerRespawnS2CPacket.class.getMethod(mapped);
				h.put(1, lookup.unreflect(m));
			}
		}
		catch (NoSuchMethodException | SecurityException | IllegalAccessException e)
		{
			Pehkui.LOGGER.error("Current name lookup: {}", mapped);
			Pehkui.LOGGER.catching(e);
		}
		
		DRAW_BOX_OUTLINE = h.get(0);
		SHOULD_KEEP_PLAYER_ATTRIBUTES = h.get(1);
	}
	
	public static boolean wasPlayerAlive(final PlayerRespawnS2CPacket packet)
	{
		if (VersionUtils.MINOR < 19 || (VersionUtils.MINOR == 19 && VersionUtils.PATCH <= 2))
		{
			if (SHOULD_KEEP_PLAYER_ATTRIBUTES != null)
			{
				try
				{
					return (boolean) SHOULD_KEEP_PLAYER_ATTRIBUTES.invoke(packet);
				}
				catch (Throwable e)
				{
					throw new RuntimeException(e);
				}
			}
		}
		
		return packet.hasFlag((byte) 1);
	}
	
	public static void renderInteractionBox(@Nullable final Object matrices, @Nullable final Object vertices, final Box box)
	{
		renderInteractionBox(matrices, vertices, box, 0.25F, 1.0F, 0.0F, 1.0F);
	}
	
	public static void renderInteractionBox(@Nullable final Object matrices, @Nullable final Object vertices, final Box box, final float red, final float green, final float blue, final float alpha)
	{
		if (VersionUtils.MINOR >= 15)
		{
			WorldRenderer.drawBox((MatrixStack) matrices, (VertexConsumer) vertices, box, red, green, blue, alpha);
		}
		else if (DRAW_BOX_OUTLINE != null)
		{
			try
			{
				DRAW_BOX_OUTLINE.invoke(box, red, green, blue, alpha);
			}
			catch (Throwable e)
			{
				throw new RuntimeException(e);
			}
		}
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
			return Math.max(depth * scale, (float) PehkuiConfig.CLIENT.minimumCameraDepth.get().doubleValue());
		}
		
		return depth;
	}
	
	public static boolean shouldSkipHeadItemScaling(@Nullable LivingEntity entity, ItemStack item, Object renderMode)
	{
		if ("HEAD".equals(((Enum<?>) renderMode).name()))
		{
			if (entity == null || (entity.getEquippedStack(EquipmentSlot.MAINHAND) != item && entity.getEquippedStack(EquipmentSlot.OFFHAND) != item))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static void logIfRenderCancelled()
	{
		logIfItemRenderCancelled(true);
		logIfEntityRenderCancelled(true);
	}
	
	private static final Set<Item> loggedItems = ConcurrentHashMap.newKeySet();
	private static ItemStack lastRenderedStack = null;
	private static int itemRecursionDepth = 0;
	private static int maxItemRecursionDepth = 2;
	
	public static void logIfItemRenderCancelled()
	{
		logIfItemRenderCancelled(false);
	}
	
	private static void logIfItemRenderCancelled(final boolean force)
	{
		if (lastRenderedStack != null && (force || itemRecursionDepth >= maxItemRecursionDepth))
		{
			final Item i = lastRenderedStack.getItem();
			if (force || !loggedItems.contains(i))
			{
				final String stackKey = lastRenderedStack.getTranslationKey();
				final String itemKey = lastRenderedStack.getItem().getTranslationKey();
				if (stackKey.equals(itemKey))
				{
					Pehkui.LOGGER.error("[{}]: Did something cancel item rendering early? Matrix stack was not popped after rendering item {} ({}).", Pehkui.MOD_ID, stackKey, lastRenderedStack.getItem());
				}
				else
				{
					Pehkui.LOGGER.error("[{}]: Did something cancel item rendering early? Matrix stack was not popped after rendering item {} ({}) ({})", Pehkui.MOD_ID, stackKey, itemKey, lastRenderedStack.getItem());
				}
				
				loggedItems.add(i);
			}
		}
	}
	
	public static void saveLastRenderedItem(final ItemStack currentStack)
	{
		if (itemRecursionDepth == 0)
		{
			lastRenderedStack = currentStack;
		}
		
		itemRecursionDepth++;
	}
	
	public static void clearLastRenderedItem()
	{
		lastRenderedStack = null;
		itemRecursionDepth = 0;
	}
	
	private static final Set<EntityType<?>> loggedEntityTypes = ConcurrentHashMap.newKeySet();
	private static EntityType<?> lastRenderedEntity = null;
	private static int entityRecursionDepth = 0;
	private static int maxEntityRecursionDepth = 2;
	
	public static void logIfEntityRenderCancelled()
	{
		logIfEntityRenderCancelled(false);
	}
	
	private static void logIfEntityRenderCancelled(final boolean force)
	{
		if (lastRenderedEntity != null && (force || entityRecursionDepth >= maxEntityRecursionDepth))
		{
			if (force || !loggedEntityTypes.contains(lastRenderedEntity))
			{
				final Identifier id = EntityType.getId(lastRenderedEntity);
				
				Pehkui.LOGGER.error("[{}]: Did something cancel entity rendering early? Matrix stack was not popped after rendering entity {}.", Pehkui.MOD_ID, id);
				
				loggedEntityTypes.add(lastRenderedEntity);
			}
		}
	}
	
	public static void saveLastRenderedEntity(final EntityType<?> type)
	{
		if (entityRecursionDepth == 0)
		{
			lastRenderedEntity = type;
		}
		
		entityRecursionDepth++;
	}
	
	public static void clearLastRenderedEntity()
	{
		lastRenderedEntity = null;
		entityRecursionDepth = 0;
	}
	
	public static void addDetailsToCrashReport(CrashReportSection section)
	{
		if (lastRenderedStack != null)
		{
			section.add("pehkui:debug/render/item", lastRenderedStack.getItem().getTranslationKey());
		}
		
		if (lastRenderedEntity != null)
		{
			final Identifier id = EntityType.getId(lastRenderedEntity);
			
			section.add("pehkui:debug/render/entity", id);
		}
	}
}
