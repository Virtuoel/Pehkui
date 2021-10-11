package virtuoel.pehkui.util;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.function.IntPredicate;

public class MulticonnectCompatibility
{
	private static final boolean MULTICONNECT_LOADED = ModLoaderUtils.isModLoaded("multiconnect");
	
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
	
	public <T> T getProtocolDependantValue(IntPredicate protocolPredicate, T trueValue, T defaultValue)
	{
		if (this.enabled)
		{
			return protocolVersion.map(f ->
			{
				try
				{
					return protocolPredicate.test((int) f.get(null)) ? trueValue : defaultValue;
				}
				catch (IllegalAccessException | IllegalArgumentException e)
				{
					return defaultValue;
				}
			}).orElse(defaultValue);
		}
		
		return defaultValue;
	}
}
