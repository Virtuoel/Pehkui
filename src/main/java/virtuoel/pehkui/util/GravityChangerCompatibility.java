package virtuoel.pehkui.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Direction;

public class GravityChangerCompatibility
{
	private static final boolean GRAVITY_CHANGER_LOADED = ModLoaderUtils.isModLoaded("gravitychanger");
	
	public static final GravityChangerCompatibility INSTANCE = new GravityChangerCompatibility();
	
	private final Optional<Class<?>> apiClass;
	private final Optional<Method> getterMethod;
	
	private final Optional<Class<?>> accessorClass;
	private final Optional<Method> accessorGetterMethod;
	
	private boolean enabled;
	
	private GravityChangerCompatibility()
	{
		this.enabled = GRAVITY_CHANGER_LOADED;
		
		if (this.enabled)
		{
			this.apiClass = ReflectionUtils.getClass("me.andrew.gravitychanger.api.GravityChangerAPI");
			this.getterMethod = ReflectionUtils.getMethod(apiClass, "getAppliedGravityDirection", PlayerEntity.class);
			
			this.accessorClass = ReflectionUtils.getClass("me.andrew.gravitychanger.accessor.EntityAccessor");
			this.accessorGetterMethod = ReflectionUtils.getMethod(accessorClass, "gravitychanger$getAppliedGravityDirection");
		}
		else
		{
			this.apiClass = Optional.empty();
			this.getterMethod = Optional.empty();
			
			this.accessorClass = Optional.empty();
			this.accessorGetterMethod = Optional.empty();
		}
	}
	
	public Direction getGravityDirection(PlayerEntity entity)
	{
		if (this.enabled)
		{
			return getterMethod.map(m ->
			{
				try
				{
					return (Direction) m.invoke(null, entity);
				}
				catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
				{
					return Direction.DOWN;
				}
			})
			.orElseGet(() ->
			{
				return accessorGetterMethod.map(m ->
				{
					try
					{
						return (Direction) m.invoke(entity);
					}
					catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
					{
						return Direction.DOWN;
					}
				})
				.orElse(Direction.DOWN);
			});
		}
		
		return Direction.DOWN;
	}
	
}
