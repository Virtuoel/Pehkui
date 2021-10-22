package virtuoel.pehkui.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import net.minecraft.entity.LivingEntity;

public class ReachEntityAttributesCompatibility
{
	public static final boolean LOADED = ModLoaderUtils.isModLoaded("reach-entity-attributes");
	
	public static final ReachEntityAttributesCompatibility INSTANCE = new ReachEntityAttributesCompatibility();
	
	private final Optional<Class<?>> mainClass;
	
	private final Optional<Method> getReachDistance;
	private final Optional<Method> getAttackRange;
	private final Optional<Method> getSquaredReachDistance;
	private final Optional<Method> getSquaredAttackRange;
	
	private boolean enabled;
	
	private ReachEntityAttributesCompatibility()
	{
		this.enabled = LOADED;
		
		if (this.enabled)
		{
			this.mainClass = ReflectionUtils.getClass("com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes");
			
			this.getReachDistance = ReflectionUtils.getMethod(mainClass, "getReachDistance", LivingEntity.class, double.class);
			this.getAttackRange = ReflectionUtils.getMethod(mainClass, "getAttackRange", LivingEntity.class, double.class);
			this.getSquaredReachDistance = ReflectionUtils.getMethod(mainClass, "getSquaredReachDistance", LivingEntity.class, double.class);
			this.getSquaredAttackRange = ReflectionUtils.getMethod(mainClass, "getSquaredAttackRange", LivingEntity.class, double.class);
		}
		else
		{
			this.mainClass = Optional.empty();
			
			this.getReachDistance = Optional.empty();
			this.getAttackRange = Optional.empty();
			this.getSquaredReachDistance = Optional.empty();
			this.getSquaredAttackRange = Optional.empty();
		}
	}
	
	public double getReachDistance(LivingEntity entity, double baseValue)
	{
		return getReachDistance(getReachDistance, entity, baseValue);
	}
	
	public double getSquaredReachDistance(LivingEntity entity, double baseValue)
	{
		return getReachDistance(getSquaredReachDistance, entity, baseValue);
	}
	
	public double getAttackRange(LivingEntity entity, double baseValue)
	{
		return getReachDistance(getAttackRange, entity, baseValue);
	}
	
	public double getSquaredAttackRange(LivingEntity entity, double baseValue)
	{
		return getReachDistance(getSquaredAttackRange, entity, baseValue);
	}
	
	private double getReachDistance(Optional<Method> method, LivingEntity entity, double baseValue)
	{
		if (this.enabled)
		{
			return method.map(m ->
			{
				try
				{
					return (double) m.invoke(null, entity, baseValue);
				}
				catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
				{
					return null;
				}
			}).orElse(baseValue);
		}
		
		return baseValue;
	}
}
