package virtuoel.pehkui.util;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.api.metadata.version.VersionPredicate;

public class ModLoaderUtils
{
	public static boolean isModLoaded(final String modId)
	{
		return FabricLoader.getInstance().isModLoaded(modId);
	}

	public static boolean isModLoaded(final String modId, final String version)
	{
		try
		{
			VersionPredicate parsedVersion = VersionPredicate.parse(version);

			return FabricLoader.getInstance().getModContainer(modId).map(c ->
			{
				return parsedVersion.test(c.getMetadata().getVersion());
			}).orElse(false);
		}
		catch (VersionParsingException e)
		{
			throw new RuntimeException(e);
		}
	}
}
