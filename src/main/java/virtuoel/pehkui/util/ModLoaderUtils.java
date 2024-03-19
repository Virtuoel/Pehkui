package virtuoel.pehkui.util;

import net.neoforged.fml.loading.FMLLoader;

public class ModLoaderUtils
{
	public static boolean isModLoaded(final String modId)
	{
		return FMLLoader.getLoadingModList().getModFileById(modId) != null;
	}
}
