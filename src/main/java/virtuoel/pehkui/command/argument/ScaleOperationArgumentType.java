package virtuoel.pehkui.command.argument;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.DoubleBinaryOperator;
import java.util.stream.Collectors;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import virtuoel.pehkui.Pehkui;
import virtuoel.pehkui.api.ScaleOperations;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.mixin.OperationArgumentTypeAccessor;

public class ScaleOperationArgumentType implements ArgumentType<ScaleOperationArgumentType.Operation>
{
	private static final Collection<String> EXAMPLES = ScaleRegistries.SCALE_OPERATIONS.keySet().stream().map(Identifier::toString).collect(Collectors.toList());
	private static final SimpleCommandExceptionType INVALID_OPERATION = OperationArgumentTypeAccessor.getInvalidOperationException();
	private static final SimpleCommandExceptionType DIVISION_ZERO_EXCEPTION = OperationArgumentTypeAccessor.getDivisionZeroException();
	
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
		return CommandSource.suggestMatching(
			ScaleRegistries.SCALE_OPERATIONS.keySet().stream().filter(id ->
			{
				return !id.equals(ScaleRegistries.getDefaultId(ScaleRegistries.SCALE_OPERATIONS));
			})
			.map(id ->
			{
				return id.getNamespace().equals(Pehkui.MOD_ID) ? id.getPath() : id.toString();
			}),
			builder);
	}
	
	@Override
	public Collection<String> getExamples()
	{
		return EXAMPLES;
	}
	
	private static Operation getOperator(String string) throws CommandSyntaxException
	{
		final DoubleBinaryOperator entry = ScaleRegistries.getEntry(
			ScaleRegistries.SCALE_OPERATIONS,
			string.contains(":") ? new Identifier(string) : Pehkui.id(string)
		);
		
		if (entry == null || entry == ScaleOperations.NOOP)
		{
			throw INVALID_OPERATION.create();
		}
		else if (entry == ScaleOperations.DIVIDE)
		{
			return (curr, arg) ->
			{
				if (arg == 0)
				{
					throw DIVISION_ZERO_EXCEPTION.create();
				}
				else
				{
					return entry.applyAsDouble(curr, arg);
				}
			};
		}
		
		return entry::applyAsDouble;
	}
	
	@FunctionalInterface
	public interface Operation
	{
		double apply(double curr, double arg) throws CommandSyntaxException;
	}
}
