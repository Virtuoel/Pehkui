package virtuoel.pehkui.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;

import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.StringTextComponent;
import virtuoel.pehkui.Pehkui;
import virtuoel.pehkui.api.ResizableEntity;

public class ScaleCommand
{
	public static void register(CommandDispatcher<ServerCommandSource> commandDispatcher)
	{
		commandDispatcher.register(
			CommandManager.literal("setscale").requires(commandSource ->
			{
				return commandSource.hasPermissionLevel(2);
			})
			.then(CommandManager.argument("entity", EntityArgumentType.entity())
				.then(CommandManager.argument("scale", FloatArgumentType.floatArg())
					.executes(context ->
					{
						try
						{
							final ResizableEntity entity = (ResizableEntity) EntityArgumentType.getEntity(context, "entity");
							entity.setTargetScale(FloatArgumentType.getFloat(context, "scale"));
							entity.scheduleScaleUpdate();
						}
						catch(Exception e)
						{
							Pehkui.LOGGER.catching(e);
							throw e;
						}
						return 1;
					})
				)
			)
		);
		
		commandDispatcher.register(
			CommandManager.literal("setscaledelay").requires(commandSource ->
			{
				return commandSource.hasPermissionLevel(2);
			})
			.then(CommandManager.argument("entity", EntityArgumentType.entity())
				.then(CommandManager.argument("ticks", IntegerArgumentType.integer())
					.executes(context ->
					{
						try
						{
							final ResizableEntity entity = (ResizableEntity) EntityArgumentType.getEntity(context, "entity");
							entity.setScaleTickDelay(IntegerArgumentType.getInteger(context, "ticks"));
							entity.scheduleScaleUpdate();
						}
						catch(Exception e)
						{
							Pehkui.LOGGER.catching(e);
							throw e;
						}
						return 1;
					})
				)
			)
		);
		
		commandDispatcher.register(
			CommandManager.literal("getscale").requires(commandSource ->
			{
				return commandSource.hasPermissionLevel(2);
			})
			.then(CommandManager.argument("entity", EntityArgumentType.entity())
				.executes(context ->
				{
					float scale = ((ResizableEntity) EntityArgumentType.getEntity(context, "entity")).getScale();
					context.getSource().sendFeedback(new StringTextComponent("Scale: " + scale), false);
					return 1;
				})
			)
		);
		
		commandDispatcher.register(
			CommandManager.literal("getscaledelay").requires(commandSource ->
			{
				return commandSource.hasPermissionLevel(2);
			})
			.then(CommandManager.argument("entity", EntityArgumentType.entity())
				.executes(context ->
				{
					int ticks = ((ResizableEntity) EntityArgumentType.getEntity(context, "entity")).getScaleTickDelay();
					context.getSource().sendFeedback(new StringTextComponent("Delay: " + ticks + " ticks"), false);
					return 1;
				})
			)
		);
	}
}
