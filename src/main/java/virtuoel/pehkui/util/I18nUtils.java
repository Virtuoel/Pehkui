package virtuoel.pehkui.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;

public class I18nUtils
{
	public static final Object[] EMPTY_VARARGS = new Object[0];
	
	private static final boolean RESOURCE_LOADER_LOADED = ModLoaderUtils.isModLoaded("fabric-resource-loader-v0");
	
	public static Text translate(final String unlocalized, final String defaultLocalized)
	{
		return translate(unlocalized, defaultLocalized, EMPTY_VARARGS);
	}
	
	private static final Constructor<?> LITERAL = ReflectionUtils.getConstructor(Optional.ofNullable(ReflectionUtils.LITERAL_TEXT), String.class).orElse(null);
	private static final Constructor<TranslatableTextContent> TRANSLATABLE = ReflectionUtils.getConstructor(Optional.of(TranslatableTextContent.class), String.class, Object[].class).orElse(null);
	
	public static Text translate(final String unlocalized, final String defaultLocalized, final Object... args)
	{
		if (RESOURCE_LOADER_LOADED)
		{
			if (VersionUtils.MINOR < 19)
			{
				try
				{
					return (Text) TRANSLATABLE.newInstance(unlocalized, args);
				}
				catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
				{
					
				}
			}
			
			return Text.translatable(unlocalized, args);
		}
		
		return literal(defaultLocalized, args);
	}
	
	public static Text literal(final String text, final Object... args)
	{
		if (VersionUtils.MINOR < 19)
		{
			try
			{
				return (Text) LITERAL.newInstance(String.format(text, args));
			}
			catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
			{
				
			}
		}
		
		return Text.literal(String.format(text, args));
	}
}
