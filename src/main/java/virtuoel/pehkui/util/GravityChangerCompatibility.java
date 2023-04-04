package virtuoel.pehkui.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Direction;

public class GravityChangerCompatibility
{
	private static final boolean GRAVITY_API_LOADED = ModLoaderUtils.isModLoaded("gravity_api");
	private static final boolean GRAVITY_CHANGER_LOADED = ModLoaderUtils.isModLoaded("gravitychanger");
	
	public static final GravityChangerCompatibility INSTANCE = new GravityChangerCompatibility();
	
	private final Optional<Method> getterMethod;
	private final Optional<Method> oldGetterMethod;
	private final Optional<Method> accessorGetterMethod;
	
	private boolean enabled;
	
	private GravityChangerCompatibility()
	{
		this.enabled = GRAVITY_API_LOADED || GRAVITY_CHANGER_LOADED;
		
		if (this.enabled)
		{
			final Optional<Class<?>> apiClass = ReflectionUtils.getClass("com.fusionflux.gravity_api.api.GravityChangerAPI");
			this.getterMethod = ReflectionUtils.getMethod(apiClass, "getGravityDirection", Entity.class);
			
			final Optional<Class<?>> oldApiClass = ReflectionUtils.getClass("me.andrew.gravitychanger.api.GravityChangerAPI");
			this.oldGetterMethod = ReflectionUtils.getMethod(oldApiClass, "getAppliedGravityDirection", PlayerEntity.class);
			
			final Optional<Class<?>> accessorClass = ReflectionUtils.getClass("me.andrew.gravitychanger.accessor.EntityAccessor");
			this.accessorGetterMethod = ReflectionUtils.getMethod(accessorClass, "gravitychanger$getAppliedGravityDirection");
		}
		else
		{
			this.getterMethod = Optional.empty();
			this.oldGetterMethod = Optional.empty();
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
				return oldGetterMethod.map(m ->
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
			});
		}
		
		return Direction.DOWN;
	}
	
	public float getXCorrection(PlayerEntity player)
	{
		if (this.enabled)
		{
			final Direction gravity = getGravityDirection(player);
			
			switch (gravity)
			{
				case WEST:
					return 1.5F;
				case EAST:
					return -1.5F;
				default:
					break;
			}
		}
		
		return 0.0F;
	}
	
	public float getYCorrection(PlayerEntity player)
	{
		if (this.enabled)
		{
			final Direction gravity = getGravityDirection(player);
			
			switch (gravity)
			{
				case UP:
					return -3.0F;
				case DOWN:
					break;
				default:
					return -1.5F;
			}
		}
		
		return 0.0F;
	}
	
	public float getZCorrection(PlayerEntity player)
	{
		if (this.enabled)
		{
			final Direction gravity = getGravityDirection(player);
			
			switch (gravity)
			{
				case NORTH:
					return 1.5F;
				case SOUTH:
					return -1.5F;
				default:
					break;
			}
		}
		
		return 0.0F;
	}
}
