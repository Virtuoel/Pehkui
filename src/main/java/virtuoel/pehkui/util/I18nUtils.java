package virtuoel.pehkui.util;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class I18nUtils
{
	public static final Object[] EMPTY_VARARGS = new Object[0];
	
	private static final boolean RESOURCE_LOADER_LOADED = ModLoaderUtils.isModLoaded("fabric-resource-loader-v0");
	
	public static Text translate(final String unlocalized, final String defaultLocalized)
	{
		return translate(unlocalized, defaultLocalized, EMPTY_VARARGS);
	}
	
	public static Text translate(final String unlocalized, final String defaultLocalized, final Object... args)
	{
		return RESOURCE_LOADER_LOADED ? new TranslatableText(unlocalized, args) : new LiteralText(String.format(defaultLocalized, args));
	}
}
