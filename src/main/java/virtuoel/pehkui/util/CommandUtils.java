package virtuoel.pehkui.util;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.serialize.ArgumentSerializer;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.predicate.NumberRange;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import virtuoel.pehkui.Pehkui;
import virtuoel.pehkui.command.argument.ScaleModifierArgumentType;
import virtuoel.pehkui.command.argument.ScaleOperationArgumentType;
import virtuoel.pehkui.command.argument.ScaleTypeArgumentType;
import virtuoel.pehkui.server.command.DebugCommand;
import virtuoel.pehkui.server.command.ScaleCommand;

public class CommandUtils
{
	public static void registerArgumentTypes()
	{
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		
		final DeferredRegister<ArgumentSerializer<?, ?>> registry = DeferredRegister.create(Registry.COMMAND_ARGUMENT_TYPE_KEY, Pehkui.MOD_ID);
		
		registry.register(
			"scale_type",
			() -> ArgumentTypes.registerByClass(
					ScaleTypeArgumentType.class,
					ConstantArgumentSerializer.of(ScaleTypeArgumentType::scaleType)
				)
		);
		registry.register(
			"scale_modifier",
			() -> ArgumentTypes.registerByClass(
					ScaleModifierArgumentType.class,
					ConstantArgumentSerializer.of(ScaleModifierArgumentType::scaleModifier)
				)
		);
		registry.register(
			"scale_operation",
			() -> ArgumentTypes.registerByClass(
					ScaleOperationArgumentType.class,
					ConstantArgumentSerializer.of(ScaleOperationArgumentType::operation)
				)
		);
		
		registry.register(modEventBus);
	}
	
	public static void registerCommands(final CommandDispatcher<ServerCommandSource> dispatcher)
	{
		ScaleCommand.register(dispatcher);
		DebugCommand.register(dispatcher);
	}
	
	public static boolean testFloatRange(NumberRange.FloatRange range, float value)
	{
		return range.test(value);
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
