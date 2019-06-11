package virtuoel.pehkui.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;

import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
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
			.then(CommandManager.argument("targets", EntityArgumentType.entities())
				.then(CommandManager.argument("scale", FloatArgumentType.floatArg())
					.executes(context ->
					{
						try
						{
							final float scale = FloatArgumentType.getFloat(context, "scale");
							for(final Entity e : EntityArgumentType.getEntities(context, "targets"))
							{
								final ResizableEntity entity = (ResizableEntity) e;
								entity.setTargetScale(scale);
								entity.scheduleScaleUpdate();
							}
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
			.then(CommandManager.argument("targets", EntityArgumentType.entities())
				.then(CommandManager.argument("ticks", IntegerArgumentType.integer())
					.executes(context ->
					{
						try
						{
							final int ticks = IntegerArgumentType.getInteger(context, "ticks");
							for(final Entity e : EntityArgumentType.getEntities(context, "targets"))
							{
								final ResizableEntity entity = (ResizableEntity) e;
								entity.setScaleTickDelay(ticks);
								entity.scheduleScaleUpdate();
							}
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
					final float scale = ((ResizableEntity) EntityArgumentType.getEntity(context, "entity")).getScale();
					context.getSource().sendFeedback(new TextComponent("Scale: " + scale), false);
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
					final int ticks = ((ResizableEntity) EntityArgumentType.getEntity(context, "entity")).getScaleTickDelay();
					context.getSource().sendFeedback(new TextComponent("Delay: " + ticks + " ticks"), false);
					return 1;
				})
			)
		);
	}
}
