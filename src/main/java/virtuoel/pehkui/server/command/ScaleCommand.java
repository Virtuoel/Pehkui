package virtuoel.pehkui.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;

import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import virtuoel.pehkui.Pehkui;
import virtuoel.pehkui.api.ScaleData;

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
								final ScaleData data = ScaleData.of(e);
								data.setTargetScale(scale);
								data.markForSync();
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
								final ScaleData data = ScaleData.of(e);
								data.setScaleTickDelay(ticks);
								data.markForSync();
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
					final float scale = ScaleData.of(EntityArgumentType.getEntity(context, "entity")).getScale();
					context.getSource().sendFeedback(new LiteralText("Scale: " + scale), false);
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
					final int ticks = ScaleData.of(EntityArgumentType.getEntity(context, "entity")).getScaleTickDelay();
					context.getSource().sendFeedback(new LiteralText("Delay: " + ticks + " ticks"), false);
					return 1;
				})
			)
		);
	}
}
