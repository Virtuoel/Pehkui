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
import net.minecraft.command.argument.EnumArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import virtuoel.pehkui.api.ScaleOperation;
import virtuoel.pehkui.mixin.OperationArgumentTypeAccessor;

public class ScaleOperationArgumentType extends EnumArgumentType<ScaleOperation>
{
	private ScaleOperationArgumentType()
	{
		super(ScaleOperation.CODEC, ScaleOperation::values);
	}

	public static ScaleOperationArgumentType operation()
	{
		return new ScaleOperationArgumentType();
	}

	public static ScaleOperation getOperation(CommandContext<ServerCommandSource> context, String id)
	{
		return context.getArgument(id, ScaleOperation.class);
	}
}
