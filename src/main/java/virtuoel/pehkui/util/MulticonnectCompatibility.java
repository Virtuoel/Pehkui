package virtuoel.pehkui.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.IntPredicate;

public class MulticonnectCompatibility
{
	private static final boolean MULTICONNECT_LOADED = ModLoaderUtils.isModLoaded("multiconnect");
	
	public static final MulticonnectCompatibility INSTANCE = new MulticonnectCompatibility();
	
	private final Optional<Class<?>> multiconnectApiClass;
	private final Optional<Method> instanceMethod;
	private final Optional<Method> protocolVersion;
	
	private boolean enabled;
	
	private MulticonnectCompatibility()
	{
		this.enabled = MULTICONNECT_LOADED;
		
		if (this.enabled)
		{
			this.multiconnectApiClass = ReflectionUtils.getClass("net.earthcomputer.multiconnect.api.MultiConnectAPI");
			
			this.instanceMethod = ReflectionUtils.getMethod(multiconnectApiClass, "instance");
			
			this.protocolVersion = ReflectionUtils.getMethod(multiconnectApiClass, "getProtocolVersion");
		}
		else
		{
			this.multiconnectApiClass = Optional.empty();
			this.instanceMethod = Optional.empty();
			this.protocolVersion = Optional.empty();
		}
	}
	
	public <T> T getProtocolDependantValue(IntPredicate protocolPredicate, T trueValue, T defaultValue)
	{
		if (this.enabled)
		{
			return instanceMethod.flatMap(m ->
			{
				return protocolVersion.map(f ->
				{
					try
					{
						return protocolPredicate.test((int) f.invoke(m.invoke(null))) ? trueValue : defaultValue;
					}
					catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
					{
						return defaultValue;
					}
				});
			})
			.orElse(defaultValue);
		}
		
		return defaultValue;
	}
}
