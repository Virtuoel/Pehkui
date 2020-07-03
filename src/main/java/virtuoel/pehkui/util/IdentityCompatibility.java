package virtuoel.pehkui.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class IdentityCompatibility
{
	private static final boolean IDENTITY_LOADED = FabricLoader.getInstance().isModLoaded("identity");
	
	public static final IdentityCompatibility INSTANCE = new IdentityCompatibility();
	
	private final Optional<Class<?>> componentsClass;
	private final Optional<Class<?>> componentTypeClass;
	private final Optional<Class<?>> identityComponentClass;
	
	private final Optional<Object> currentIdentity;
	
	private final Optional<Method> getComponent;
	private final Optional<Method> getIdentity;
	
	private boolean enabled;
	
	public IdentityCompatibility()
	{
		this.enabled = IDENTITY_LOADED;
		
		if (this.enabled)
		{
			this.componentsClass = getClass("draylar.identity.registry.Components");
			this.componentTypeClass = getClass("nerdhub.cardinal.components.api.ComponentType");
			this.identityComponentClass = getClass("draylar.identity.cca.IdentityComponent");
			
			this.currentIdentity = getField(componentsClass, "CURRENT_IDENTITY").map(f ->
			{
				try
				{
					return f.get(null);
				}
				catch (IllegalArgumentException | IllegalAccessException e)
				{
					return Optional.empty();
				}
			});
			
			this.getComponent = getMethod(componentTypeClass, "get", Object.class);
			this.getIdentity = getMethod(identityComponentClass, "getIdentity");
		}
		else
		{
			this.componentsClass = Optional.empty();
			this.componentTypeClass = Optional.empty();
			this.identityComponentClass = Optional.empty();
			
			this.currentIdentity = Optional.empty();
			
			this.getComponent = Optional.empty();
			this.getIdentity = Optional.empty();
		}
	}
	
	public LivingEntity getIdentity(PlayerEntity entity)
	{
		if (this.enabled)
		{
			return currentIdentity.flatMap(t ->
			{
				return getComponent.flatMap(c ->
				{
					return getIdentity.map(m ->
					{
						try
						{
							return (LivingEntity) m.invoke(c.invoke(t, entity));
						}
						catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
						{
							return null;
						}
					});
				});
			}).orElse(null);
		}
		
		return null;
	}
	
	private static Optional<Field> getField(final Optional<Class<?>> classObj, final String fieldName)
	{
		return classObj.map(c ->
		{
			try
			{
				final Field f = c.getDeclaredField(fieldName);
				f.setAccessible(true);
				return f;
			}
			catch (SecurityException | NoSuchFieldException e)
			{
				
			}
			return null;
		});
	}
	
	private static Optional<Method> getMethod(final Optional<Class<?>> classObj, final String methodName, Class<?>... args)
	{
		return classObj.map(c ->
		{
			try
			{
				final Method m = c.getMethod(methodName, args);
				m.setAccessible(true);
				return m;
			}
			catch (SecurityException | NoSuchMethodException e)
			{
				
			}
			return null;
		});
	}
	
	private static Optional<Class<?>> getClass(final String className)
	{
		try
		{
			return Optional.of(Class.forName(className));
		}
		catch (ClassNotFoundException e)
		{
			
		}
		return Optional.empty();
	}
}
