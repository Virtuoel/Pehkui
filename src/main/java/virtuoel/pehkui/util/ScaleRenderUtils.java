package virtuoel.pehkui.util;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.math.Box;
import virtuoel.pehkui.Pehkui;
import virtuoel.pehkui.api.PehkuiConfig;
import virtuoel.pehkui.mixin.client.compat114.WorldRendererAccessor;

public class ScaleRenderUtils
{
	private static boolean loggedWrongVersionCall = false;
	
	public static void renderInteractionBox(final Box box)
	{
		renderInteractionBox(box, 0.25F, 1.0F, 0.0F, 1.0F);
	}
	
	public static void renderInteractionBox(final Box box, final float red, final float green, final float blue, final float alpha)
	{
		if (VersionUtils.MINOR < 15)
		{
			WorldRendererAccessor.pehkui$drawBoxOutline(box, red, green, blue, alpha);
		}
		else if (!loggedWrongVersionCall)
		{
			Pehkui.LOGGER.warn("Called outline render method for wrong version (1.{}), but currently on 1.{}.x", "14.x", VersionUtils.MINOR);
			loggedWrongVersionCall = true;
		}
	}
	
	public static void renderInteractionBox(final Object matrices, final Object vertices, final Box box)
	{
		renderInteractionBox(matrices, vertices, box, 0.25F, 1.0F, 0.0F, 1.0F);
	}
	
	public static void renderInteractionBox(final Object matrices, final Object vertices, final Box box, final float red, final float green, final float blue, final float alpha)
	{
		if (VersionUtils.MINOR > 14)
		{
			WorldRenderer.drawBox((MatrixStack) matrices, (VertexConsumer) vertices, box, red, green, blue, alpha);
		}
		else if (!loggedWrongVersionCall)
		{
			Pehkui.LOGGER.warn("Called outline render method for wrong version (1.{}), but currently on 1.{}.x", "15+", VersionUtils.MINOR);
			loggedWrongVersionCall = true;
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
	
	private static final Set<Item> loggedItems = ConcurrentHashMap.newKeySet();
	private static ItemStack lastRenderedStack = null;
	private static int itemRecursionDepth = 0;
	private static int maxItemRecursionDepth = 2;
	
	public static void logIfItemRenderCancelled()
	{
		if (lastRenderedStack != null && itemRecursionDepth >= maxItemRecursionDepth)
		{
			final Item i = lastRenderedStack.getItem();
			if (!loggedItems.contains(i))
			{
				final String stackKey = lastRenderedStack.getTranslationKey();
				final String itemKey = lastRenderedStack.getItem().getTranslationKey();
				if (stackKey.equals(itemKey))
				{
					Pehkui.LOGGER.fatal("[{}]: Did something cancel item rendering early? Matrix stack was not popped after rendering item {} ({}).", Pehkui.MOD_ID, stackKey, lastRenderedStack.getItem());
				}
				else
				{
					Pehkui.LOGGER.fatal("[{}]: Did something cancel item rendering early? Matrix stack was not popped after rendering item {} ({}) ({})", Pehkui.MOD_ID, stackKey, itemKey, lastRenderedStack.getItem());
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
		if (lastRenderedEntity != null && entityRecursionDepth >= maxEntityRecursionDepth)
		{
			if (!loggedEntityTypes.contains(lastRenderedEntity))
			{
				final Identifier id = EntityType.getId(lastRenderedEntity);
				
				Pehkui.LOGGER.fatal("[{}]: Did something cancel entity rendering early? Matrix stack was not popped after rendering entity {}.", Pehkui.MOD_ID, id);
				
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
