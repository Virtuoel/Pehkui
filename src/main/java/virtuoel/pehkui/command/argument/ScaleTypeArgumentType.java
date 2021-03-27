package virtuoel.pehkui.command.argument;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import virtuoel.pehkui.Pehkui;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.util.CommandUtils;

public class ScaleTypeArgumentType implements ArgumentType<ScaleType>
{
	private static final Collection<String> EXAMPLES = Arrays.asList("base", Pehkui.MOD_ID + ":base");
	public static final DynamicCommandExceptionType INVALID_ENTRY_EXCEPTION = new DynamicCommandExceptionType(arg -> new LiteralText("Unknown scale type '" + arg + "'"));
	
	@Override
	public ScaleType parse(StringReader stringReader) throws CommandSyntaxException
	{
		final Identifier identifier = Identifier.fromCommandInput(stringReader);
		return Optional.ofNullable(ScaleRegistries.getEntry(ScaleRegistries.SCALE_TYPES, identifier)).orElseThrow(() -> INVALID_ENTRY_EXCEPTION.create(identifier));
	}
	
	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder)
	{
		return CommandUtils.suggestIdentifiersIgnoringNamespace(Pehkui.MOD_ID, ScaleRegistries.SCALE_TYPES.keySet(), builder);
	}
	
	@Override
	public Collection<String> getExamples()
	{
		return EXAMPLES;
	}
	
	public static ScaleTypeArgumentType scaleType()
	{
		return new ScaleTypeArgumentType();
	}
	
	public static ScaleType getScaleTypeArgument(CommandContext<ServerCommandSource> context, String name)
	{
		return context.getArgument(name, ScaleType.class);
	}
}
