package virtuoel.pehkui.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class IdentityCompatibility
{
	private static final boolean IDENTITY_LOADED = ModLoaderUtils.isModLoaded("identity");
	
	public static final IdentityCompatibility INSTANCE = new IdentityCompatibility();
	
	private final Optional<Class<?>> componentsClass;
	private final Optional<Class<?>> componentTypeClass;
	private final Optional<Class<?>> identityComponentClass;
	private final Optional<Class<?>> playerIdentityClass;
	
	private final Optional<Object> currentIdentity;
	
	private final Optional<Method> getComponent;
	private final Optional<Method> getIdentity;
	
	private boolean enabled;
	
	private IdentityCompatibility()
	{
		this.enabled = IDENTITY_LOADED;
		
		if (this.enabled)
		{
			this.componentsClass = ReflectionUtils.getClass("draylar.identity.registry.Components");
			this.componentTypeClass = ReflectionUtils.getClass("nerdhub.cardinal.components.api.ComponentType");
			this.identityComponentClass = ReflectionUtils.getClass("draylar.identity.cca.IdentityComponent");
			this.playerIdentityClass = ReflectionUtils.getClass("draylar.identity.api.PlayerIdentity");
			
			this.currentIdentity = ReflectionUtils.getField(componentsClass, "CURRENT_IDENTITY").map(f ->
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
			
			this.getComponent = ReflectionUtils.getMethod(componentTypeClass, "get", Object.class);
			this.getIdentity = ReflectionUtils.getMethod(identityComponentClass.isPresent() ? identityComponentClass : playerIdentityClass, "getIdentity");
		}
		else
		{
			this.componentsClass = Optional.empty();
			this.componentTypeClass = Optional.empty();
			this.identityComponentClass = Optional.empty();
			this.playerIdentityClass = Optional.empty();
			
			this.currentIdentity = Optional.empty();
			
			this.getComponent = Optional.empty();
			this.getIdentity = Optional.empty();
		}
	}
	
	public LivingEntity getIdentity(final PlayerEntity entity)
	{
		if (this.enabled)
		{
			return (identityComponentClass.isPresent() ? currentIdentity.flatMap(t ->
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
				}) : getIdentity.map(m ->
				{
					try
					{
						return (LivingEntity) m.invoke(null, entity);
					}
					catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
					{
						return null;
					}
				})).orElse(null);
		}
		
		return null;
	}
}
