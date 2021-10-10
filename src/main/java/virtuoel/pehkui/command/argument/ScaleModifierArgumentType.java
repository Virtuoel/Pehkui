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
import net.minecraft.util.Identifier;
import virtuoel.pehkui.Pehkui;
import virtuoel.pehkui.api.ScaleModifier;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.util.CommandUtils;
import virtuoel.pehkui.util.I18nUtils;

public class ScaleModifierArgumentType implements ArgumentType<ScaleModifier>
{
	private static final Collection<String> EXAMPLES = Arrays.asList("identity", Pehkui.MOD_ID + ":identity");
	public static final DynamicCommandExceptionType INVALID_ENTRY_EXCEPTION = new DynamicCommandExceptionType(arg ->
	{
		return I18nUtils.translate("argument.pehkui.modifier.invalid", "Unknown scale modifier '%s'", arg);
	});
	
	@Override
	public ScaleModifier parse(StringReader stringReader) throws CommandSyntaxException
	{
		final Identifier identifier = Identifier.fromCommandInput(stringReader);
		return Optional.ofNullable(ScaleRegistries.getEntry(ScaleRegistries.SCALE_MODIFIERS, identifier)).orElseThrow(() -> INVALID_ENTRY_EXCEPTION.create(identifier));
	}
	
	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder)
	{
		return CommandUtils.suggestIdentifiersIgnoringNamespace(Pehkui.MOD_ID, ScaleRegistries.SCALE_MODIFIERS.keySet(), builder);
	}
	
	@Override
	public Collection<String> getExamples()
	{
		return EXAMPLES;
	}
	
	public static ScaleModifierArgumentType scaleModifier()
	{
		return new ScaleModifierArgumentType();
	}
	
	public static ScaleModifier getScaleModifierArgument(CommandContext<ServerCommandSource> context, String name)
	{
		return context.getArgument(name, ScaleModifier.class);
	}
}
