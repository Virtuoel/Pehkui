package virtuoel.pehkui.command.argument;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import virtuoel.pehkui.api.ScaleOperation;
import virtuoel.pehkui.mixin.OperationArgumentTypeAccessor;

public class ScaleOperationArgumentType implements ArgumentType<ScaleOperation>
{
	private static final String[] SUGGESTIONS = Arrays.stream(ScaleOperation.values()).map(ScaleOperation::asString).toArray(String[]::new);
	
	private static final Collection<String> EXAMPLES = Arrays.asList(SUGGESTIONS);
	private static final SimpleCommandExceptionType INVALID_OPERATION = OperationArgumentTypeAccessor.getInvalidOperationException();
	
	public static ScaleOperationArgumentType operation()
	{
		return new ScaleOperationArgumentType();
	}
	
	public static ScaleOperation getOperation(CommandContext<ServerCommandSource> commandContext, String string) throws CommandSyntaxException
	{
		return commandContext.getArgument(string, ScaleOperation.class);
	}
	
	@Override
	public ScaleOperation parse(StringReader stringReader) throws CommandSyntaxException
	{
		if (!stringReader.canRead())
		{
			throw INVALID_OPERATION.create();
		}
		else
		{
			int i = stringReader.getCursor();
			
			while (stringReader.canRead() && stringReader.peek() != ' ')
			{
				stringReader.skip();
			}
			
			return getOperator(stringReader.getString().substring(i, stringReader.getCursor()));
		}
	}
	
	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder)
	{
		return CommandSource.suggestMatching(SUGGESTIONS, builder);
	}
	
	@Override
	public Collection<String> getExamples()
	{
		return EXAMPLES;
	}
	
	private static ScaleOperation getOperator(String string) throws CommandSyntaxException
	{
		return Arrays.stream(ScaleOperation.values())
				.filter(op -> op.asString().equals(string))
				.findFirst()
				.orElseThrow(INVALID_OPERATION::create);
	}
}
