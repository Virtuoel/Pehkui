package virtuoel.pehkui.util;

import java.lang.reflect.Field;
import java.util.Optional;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.SharedConstants;

public class MulticonnectCompatibility
{
	private static final boolean MULTICONNECT_LOADED = FabricLoader.getInstance().isModLoaded("multiconnect");
	
	public static final MulticonnectCompatibility INSTANCE = new MulticonnectCompatibility();
	
	private final Optional<Class<?>> connectionInfoClass;
	
	private final Optional<Field> protocolVersion;
	
	private boolean enabled;
	
	public MulticonnectCompatibility()
	{
		this.enabled = MULTICONNECT_LOADED;
		
		if (this.enabled)
		{
			this.connectionInfoClass = ReflectionUtils.getClass("net.earthcomputer.multiconnect.impl.ConnectionInfo");
			
			this.protocolVersion = ReflectionUtils.getField(connectionInfoClass, "protocolVersion");
		}
		else
		{
			this.connectionInfoClass = Optional.empty();
			this.protocolVersion = Optional.empty();
		}
	}
	
	public int getProtocolVersion()
	{
		final int version = SharedConstants.getGameVersion().getProtocolVersion();
		
		if (this.enabled)
		{
			return protocolVersion.map(f ->
			{
				try
				{
					return (int) f.get(null);
				}
				catch (IllegalAccessException | IllegalArgumentException e)
				{
					return version;
				}
			}).orElse(version);
		}
		
		return version;
	}
}
