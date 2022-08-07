package virtuoel.pehkui.util;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import virtuoel.pehkui.Pehkui;
import virtuoel.pehkui.api.PehkuiConfig;

public class ScaleRenderUtils
{
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
	
	private static final Set<Item> loggedItems = ConcurrentHashMap.newKeySet();
	private static ItemStack lastRenderedStack = null;
	
	public static void logIfCancelledRender()
	{
		if (lastRenderedStack != null)
		{
			final Item i = lastRenderedStack.getItem();
			if (!loggedItems.contains(i))
			{
				final String stackKey = lastRenderedStack.getTranslationKey();
				final String itemKey = lastRenderedStack.getItem().getTranslationKey();
				if (stackKey.equals(itemKey))
				{
					Pehkui.LOGGER.fatal("[{}]: Was item rendering cancelled? Matrix stack not popped after rendering item {} ({}).", Pehkui.MOD_ID, stackKey, lastRenderedStack.getItem());
				}
				else
				{
					Pehkui.LOGGER.fatal("[{}]: Was item rendering cancelled? Matrix stack not popped after rendering item {} ({}) ({})", Pehkui.MOD_ID, stackKey, itemKey, lastRenderedStack.getItem());
				}
				
				loggedItems.add(i);
			}
		}
	}
	
	public static void saveLastRenderedItem(final ItemStack currentStack)
	{
		lastRenderedStack = currentStack;
	}
	
	public static void clearLastRenderedItem()
	{
		lastRenderedStack = null;
	}
}
