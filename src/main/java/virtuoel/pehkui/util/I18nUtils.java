package virtuoel.pehkui.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import net.minecraft.text.LiteralTextContent;
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
	
	private static final Function<String, Object> LITERAL = LiteralTextContent::new;
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
		
		if (VersionUtils.MINOR < 19)
		{
			return (Text) LITERAL.apply(String.format(defaultLocalized, args));
		}
		
		return literal(defaultLocalized, args);
	}
	
	public static Text literal(final String text, final Object... args)
	{
		if (VersionUtils.MINOR < 19)
		{
			return (Text) LITERAL.apply(String.format(text, args));
		}
		
		return Text.literal(String.format(text, args));
	}
}
