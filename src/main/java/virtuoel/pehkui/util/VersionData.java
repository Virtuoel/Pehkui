package virtuoel.pehkui.util;

import javax.annotation.Nullable;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.api.Version;

public class VersionData
{
	@Nullable
	public static final SemanticVersion MINECRAFT_VERSION = lookupMinecraftVersion();
	public static final int MAJOR = getVersionComponent(0);
	public static final int MINOR = getVersionComponent(1);
	public static final int PATCH = getVersionComponent(2);
	
	private static SemanticVersion lookupMinecraftVersion()
	{
		final Version version = FabricLoader.getInstance().getModContainer("minecraft").get().getMetadata().getVersion();
		
		return (SemanticVersion) (version instanceof SemanticVersion ? version : null);
	}
	
	private static int getVersionComponent(int pos)
	{
		return MINECRAFT_VERSION != null ? MINECRAFT_VERSION.getVersionComponent(pos) : -1;
	}
}
