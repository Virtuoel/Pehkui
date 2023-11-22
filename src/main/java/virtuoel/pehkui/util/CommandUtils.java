package virtuoel.pehkui.util;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.serialize.ArgumentSerializer;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.predicate.NumberRange;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.registries.DeferredRegister;
import virtuoel.pehkui.Pehkui;
import virtuoel.pehkui.command.argument.ScaleEasingArgumentType;
import virtuoel.pehkui.command.argument.ScaleModifierArgumentType;
import virtuoel.pehkui.command.argument.ScaleOperationArgumentType;
import virtuoel.pehkui.command.argument.ScaleTypeArgumentType;
import virtuoel.pehkui.server.command.DebugCommand;
import virtuoel.pehkui.server.command.ScaleCommand;

public class CommandUtils
{
	private static final DeferredRegister<ArgumentSerializer<?, ?>> COMMAND_ARGUMENT_TYPES = DeferredRegister.create(RegistryKeys.COMMAND_ARGUMENT_TYPE, Pehkui.MOD_ID);
	
	public static void registerArgumentTypes()
	{
		registerArgumentTypes(CommandUtils::registerConstantArgumentType);
		
		COMMAND_ARGUMENT_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
	
	public static void registerCommands(final CommandDispatcher<ServerCommandSource> dispatcher)
	{
		ScaleCommand.register(dispatcher);
		DebugCommand.register(dispatcher);
	}
	
	public static void registerArgumentTypes(ArgumentTypeConsumer consumer)
	{
		consumer.register(Pehkui.id("scale_type"), ScaleTypeArgumentType.class, ScaleTypeArgumentType::scaleType);
		consumer.register(Pehkui.id("scale_modifier"), ScaleModifierArgumentType.class, ScaleModifierArgumentType::scaleModifier);
		consumer.register(Pehkui.id("scale_operation"), ScaleOperationArgumentType.class, ScaleOperationArgumentType::operation);
		consumer.register(Pehkui.id("scale_easing"), ScaleEasingArgumentType.class, ScaleEasingArgumentType::scaleEasing);
	}
	
	@FunctionalInterface
	public interface ArgumentTypeConsumer
	{
		<T extends ArgumentType<?>> void register(Identifier id, Class<T> argClass, Supplier<T> supplier);
	}
	
	public static boolean testFloatRange(NumberRange.DoubleRange range, float value)
	{
		return range.test(value);
	}
	
	public static void sendFeedback(ServerCommandSource source, Supplier<Text> text, boolean broadcastToOps)
	{
		source.sendFeedback(text, broadcastToOps);
	}
	
	public static <T extends ArgumentType<?>> void registerConstantArgumentType(Identifier id, Class<T> argClass, Supplier<T> supplier)
	{
		COMMAND_ARGUMENT_TYPES.register(
			id.getPath(),
			() -> ArgumentTypes.registerByClass(
					argClass,
					ConstantArgumentSerializer.of(supplier)
				)
		);
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
