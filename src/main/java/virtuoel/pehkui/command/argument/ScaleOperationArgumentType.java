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
import net.minecraft.text.TranslatableText;

public class ScaleOperationArgumentType implements ArgumentType<ScaleOperationArgumentType.Operation>
{
	private static final Collection<String> EXAMPLES = Arrays.asList("set", "add", "subtract", "multiply", "divide");
	private static final SimpleCommandExceptionType INVALID_OPERATION = new SimpleCommandExceptionType(
		new TranslatableText("arguments.operation.invalid", new Object[0])
	);
	private static final SimpleCommandExceptionType DIVISION_ZERO_EXCEPTION = new SimpleCommandExceptionType(
		new TranslatableText("arguments.operation.div0", new Object[0])
	);
	
	public static ScaleOperationArgumentType operation()
	{
		return new ScaleOperationArgumentType();
	}
	
	public static Operation getOperation(CommandContext<ServerCommandSource> commandContext, String string) throws CommandSyntaxException
	{
		return commandContext.getArgument(string, ScaleOperationArgumentType.Operation.class);
	}
	
	@Override
	public Operation parse(StringReader stringReader) throws CommandSyntaxException
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
		return CommandSource.suggestMatching(new String[] { "set", "add", "subtract", "multiply", "divide" }, builder);
	}
	
	@Override
	public Collection<String> getExamples()
	{
		return EXAMPLES;
	}
	
	private static Operation getOperator(String string) throws CommandSyntaxException
	{
		switch (string)
		{
			case "set":
				return (i, j) ->
				{
					return j;
				};
			case "add":
				return (i, j) ->
				{
					return i + j;
				};
			case "subtract":
				return (i, j) ->
				{
					return i - j;
				};
			case "multiply":
				return (i, j) ->
				{
					return i * j;
				};
			case "divide":
				return (i, j) ->
				{
					if (j == 0)
					{
						throw DIVISION_ZERO_EXCEPTION.create();
					}
					else
					{
						return i / j;
					}
				};
			default:
				throw INVALID_OPERATION.create();
		}
	}
	
	@FunctionalInterface
	public interface Operation
	{
		float apply(float scaleData, float value) throws CommandSyntaxException;
	}
}
