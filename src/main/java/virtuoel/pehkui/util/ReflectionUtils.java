package virtuoel.pehkui.util;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.predicate.NumberRange;
import net.minecraft.server.network.PlayerAssociatedNetworkHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import virtuoel.pehkui.Pehkui;

public final class ReflectionUtils
{
	public static final Class<?> LITERAL_TEXT;
	public static final MethodHandle GET_FLYING_SPEED, SET_FLYING_SPEED, GET_MOUNTED_HEIGHT_OFFSET, SEND_PACKET, IS_DUMMY;
	
	static
	{
		final MappingResolver mappingResolver = FabricLoader.getInstance().getMappingResolver();
		final Int2ObjectMap<MethodHandle> h = new Int2ObjectArrayMap<MethodHandle>();
		
		final Lookup lookup = MethodHandles.lookup();
		String mapped = "unset";
		Class<?>[] c = new Class<?>[1];
		Method m;
		Field f;
		
		try
		{
			final boolean is117Plus = VersionUtils.MINOR >= 17;
			final boolean is118Minus = VersionUtils.MINOR <= 18;
			final boolean is1193Minus = VersionUtils.MINOR < 19 || (VersionUtils.MINOR == 19 && VersionUtils.PATCH <= 3);
			final boolean is1201Minus = VersionUtils.MINOR < 20 || (VersionUtils.MINOR == 20 && VersionUtils.PATCH <= 1);
			
			if (is118Minus)
			{
				mapped = mappingResolver.mapClassName("intermediary", "net.minecraft.class_2585");
				c[0] = Class.forName(mapped);
			}
			
			if (is1193Minus)
			{
				mapped = mappingResolver.mapFieldName("intermediary", "net.minecraft.class_1309", "field_6281", "F");
				f = LivingEntity.class.getField(mapped);
				f.setAccessible(true);
				h.put(0, lookup.unreflectGetter(f));
				h.put(1, lookup.unreflectSetter(f));
			}
			
			if (is1201Minus)
			{
				mapped = mappingResolver.mapMethodName("intermediary", "net.minecraft.class_1297", "method_5621", "()D");
				m = Entity.class.getMethod(mapped);
				h.put(2, lookup.unreflect(m));
				
				mapped = mappingResolver.mapMethodName("intermediary", is117Plus ? "net.minecraft.class_5629" : "net.minecraft.class_3244", "method_14364", "(Lnet/minecraft/class_2596;)V");
				m = (is117Plus ? PlayerAssociatedNetworkHandler.class : ServerPlayNetworkHandler.class).getMethod(mapped, Packet.class);
				h.put(3, lookup.unreflect(m));
				
				mapped = mappingResolver.mapMethodName("intermediary", "net.minecraft.class_2096", "method_9041", "()Z");
				m = NumberRange.class.getMethod(mapped);
				h.put(4, lookup.unreflect(m));
			}
		}
		catch (NoSuchMethodException | SecurityException | ClassNotFoundException | IllegalAccessException | NoSuchFieldException e)
		{
			Pehkui.LOGGER.error("Current name lookup: {}", mapped);
			Pehkui.LOGGER.catching(e);
		}
		
		LITERAL_TEXT = c[0];
		GET_FLYING_SPEED = h.get(0);
		SET_FLYING_SPEED = h.get(1);
		GET_MOUNTED_HEIGHT_OFFSET = h.get(2);
		SEND_PACKET = h.get(3);
		IS_DUMMY = h.get(4);
	}
	
	public static float getFlyingSpeed(final LivingEntity entity)
	{
		try
		{
			return (float) GET_FLYING_SPEED.invoke(entity);
		}
		catch (final Throwable e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public static void setFlyingSpeed(final LivingEntity entity, final float speed)
	{
		try
		{
			 SET_FLYING_SPEED.invoke(entity, speed);
		}
		catch (final Throwable e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public static double getMountedHeightOffset(final Entity entity)
	{
		if (GET_MOUNTED_HEIGHT_OFFSET != null)
		{
			try
			{
				return (double) GET_MOUNTED_HEIGHT_OFFSET.invoke(entity);
			}
			catch (final Throwable e)
			{
				throw new RuntimeException(e);
			}
		}
		
		return entity.getDimensions(entity.getPose()).height * 0.75;
	}
	
	public static void sendPacket(final ServerPlayNetworkHandler handler, final Packet<?> packet)
	{
		if (SEND_PACKET != null)
		{
			try
			{
				if (VersionUtils.MINOR <= 16)
				{
					SEND_PACKET.invoke(handler, packet);
				}
				else
				{
					SEND_PACKET.invoke((PlayerAssociatedNetworkHandler) (Object) handler, packet);
				}
			}
			catch (final Throwable e)
			{
				throw new RuntimeException(e);
			}
			
			return;
		}
		
		handler.sendPacket(packet);
	}
	
	public static boolean isDummy(final NumberRange<?> range)
	{
		if (IS_DUMMY != null)
		{
			try
			{
				return (boolean) IS_DUMMY.invoke(range);
			}
			catch (final Throwable e)
			{
				throw new RuntimeException(e);
			}
		}
		
		return range.isDummy();
	}
	
	public static Optional<Field> getField(final Optional<Class<?>> classObj, final String fieldName)
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
	
	public static void setField(final Optional<Class<?>> classObj, final String fieldName, Object object, Object value)
	{
		ReflectionUtils.getField(classObj, fieldName).ifPresent(f ->
		{
			try
			{
				f.set(object, value);
			}
			catch (IllegalArgumentException | IllegalAccessException e)
			{
				
			}
		});
	}
	
	public static Optional<Method> getMethod(final Optional<Class<?>> classObj, final String methodName, Class<?>... args)
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
	
	public static <T> Optional<Constructor<T>> getConstructor(final Optional<Class<T>> clazz, final Class<?>... params)
	{
		return clazz.map(c ->
		{
			try
			{
				return c.getConstructor(params);
			}
			catch (NoSuchMethodException | SecurityException e)
			{
				return null;
			}
		});
	}
	
	public static Optional<Class<?>> getClass(final String className, final String... classNames)
	{
		Optional<Class<?>> ret = getClass(className);
		
		for (final String name : classNames)
		{
			if (ret.isPresent())
			{
				return ret;
			}
			
			ret = getClass(name);
		}
		
		return ret;
	}
	
	public static Optional<Class<?>> getClass(final String className)
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
	
	private ReflectionUtils()
	{
		
	}
}
