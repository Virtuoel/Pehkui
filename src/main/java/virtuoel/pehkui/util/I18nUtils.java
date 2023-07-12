package virtuoel.pehkui.util;

import net.minecraft.text.Text;

public class I18nUtils
{
	public static final Object[] EMPTY_VARARGS = new Object[0];
	
	public static Text translate(final String unlocalized, final String defaultLocalized)
	{
		return translate(unlocalized, defaultLocalized, EMPTY_VARARGS);
	}
	
	public static Text translate(final String unlocalized, final String defaultLocalized, final Object... args)
	{
		return Text.translatable(unlocalized, args);
	}
	
	public static Text literal(final String text, final Object... args)
	{
		return Text.literal(String.format(text, args));
	}
}
