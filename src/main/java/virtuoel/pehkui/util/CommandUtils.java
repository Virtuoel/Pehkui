package virtuoel.pehkui.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.serialize.ArgumentSerializer;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.predicate.NumberRange;
import net.minecraft.util.Identifier;
import virtuoel.pehkui.Pehkui;
import virtuoel.pehkui.command.argument.ScaleModifierArgumentType;
import virtuoel.pehkui.command.argument.ScaleOperationArgumentType;
import virtuoel.pehkui.command.argument.ScaleTypeArgumentType;

public class CommandUtils
{
	public static void registerArgumentTypes(ArgumentTypeConsumer<ArgumentType<?>> consumer)
	{
		consumer.register(Pehkui.id("scale_type").toString(), ScaleTypeArgumentType.class, ScaleTypeArgumentType::scaleType);
		consumer.register(Pehkui.id("scale_modifier").toString(), ScaleModifierArgumentType.class, ScaleModifierArgumentType::scaleModifier);
		consumer.register(Pehkui.id("scale_operation").toString(), ScaleOperationArgumentType.class, ScaleOperationArgumentType::operation);
	}
	
	@FunctionalInterface
	public interface ArgumentTypeConsumer<T extends ArgumentType<?>>
	{
		void register(String id, Class<? extends T> argClass, Supplier<T> supplier);
	}
	
	public static final Method REGISTER_ARGUMENT_TYPE, TEST_FLOAT_RANGE;
	
	static
	{
		final MappingResolver mappingResolver = FabricLoader.getInstance().getMappingResolver();
		final Int2ObjectMap<Method> h = new Int2ObjectArrayMap<Method>();
		
		String mapped = "unset";
		
		try
		{
			final boolean is116Minus = VersionUtils.MINOR <= 16;
			final boolean is118Minus = VersionUtils.MINOR <= 18;
			
			if (is118Minus)
			{
				mapped = mappingResolver.mapMethodName("intermediary", "net.minecraft.class_2316", "method_10017", "(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/class_2314;)V");
				h.put(0, ArgumentTypes.class.getMethod(mapped, String.class, Class.class, ArgumentSerializer.class));
			}
			
			mapped = mappingResolver.mapMethodName("intermediary", "net.minecraft.class_2096$class_2099", "method_9047", is116Minus ? "(F)Z" : "(D)Z");
			h.put(1, NumberRange.FloatRange.class.getMethod(mapped, is116Minus ? float.class : double.class));
		}
		catch (NoSuchMethodException | SecurityException e1)
		{
			Pehkui.LOGGER.error("Last method lookup: {}", mapped);
			Pehkui.LOGGER.catching(e1);
		}
		
		REGISTER_ARGUMENT_TYPE = h.get(0);
		TEST_FLOAT_RANGE = h.get(1);
	}
	
	public static boolean testFloatRange(NumberRange.FloatRange range, float value)
	{
		try
		{
			if (TEST_FLOAT_RANGE != null)
			{
				return (boolean) TEST_FLOAT_RANGE.invoke(range, value);
			}
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			Pehkui.LOGGER.catching(e);
		}
		
		return false;
	}
	
	public static <T extends ArgumentType<?>> void registerConstantArgumentType(String id, Class<? extends T> argClass, Supplier<T> supplier)
	{
		if (REGISTER_ARGUMENT_TYPE != null)
		{
			try
			{
				REGISTER_ARGUMENT_TYPE.invoke(null, id, argClass, ConstantArgumentSerializer.class.getConstructor(Supplier.class).newInstance(supplier));
			}
			catch (Throwable e)
			{
				Pehkui.LOGGER.catching(e);
			}
		}
	}
	
	public static CompletableFuture<Suggestions> suggestIdentifiersIgnoringNamespace(String namespace, Iterable<Identifier> candidates, SuggestionsBuilder builder)
	{
		forEachMatchingIgnoringNamespace(
			namespace,
			candidates,
			builder.getRemaining().toLowerCase(Locale.ROOT),
			Function.identity(),
			id -> builder.suggest(String.valueOf(id))
		);
		
		return builder.buildFuture();
	}
	
	public static <T> void forEachMatchingIgnoringNamespace(String namespace, Iterable<T> candidates, String string, Function<T, Identifier> idFunc, Consumer<T> action)
	{
		final boolean hasColon = string.indexOf(':') > -1;
		
		Identifier id;
		for (final T object : candidates)
		{
			id = idFunc.apply(object);
			if (hasColon)
			{
				if (wordStartsWith(string, id.toString(), '_'))
				{
					action.accept(object);
				}
			}
			else if (
				wordStartsWith(string, id.getNamespace(), '_') ||
				id.getNamespace().equals(namespace) &&
				wordStartsWith(string, id.getPath(), '_')
			)
			{
				action.accept(object);
			}
		}
	}
	
	public static boolean wordStartsWith(String string, String substring, char wordSeparator)
	{
		for (int i = 0; !substring.startsWith(string, i); i++)
		{
			i = substring.indexOf(wordSeparator, i);
			if (i < 0)
			{
				return false;
			}
		}
		
		return true;
	}
}
