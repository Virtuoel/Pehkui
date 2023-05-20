package virtuoel.pehkui.server.command;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.SortedSet;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import net.minecraft.command.EntityDataObject;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraftforge.fml.loading.FMLLoader;
import virtuoel.pehkui.api.PehkuiConfig;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleModifier;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.api.ScaleTypes;
import virtuoel.pehkui.command.argument.ScaleEasingArgumentType;
import virtuoel.pehkui.command.argument.ScaleModifierArgumentType;
import virtuoel.pehkui.command.argument.ScaleOperationArgumentType;
import virtuoel.pehkui.command.argument.ScaleTypeArgumentType;
import virtuoel.pehkui.mixin.DataCommandInvoker;
import virtuoel.pehkui.util.CommandUtils;
import virtuoel.pehkui.util.I18nUtils;
import virtuoel.pehkui.util.PehkuiEntityExtensions;

public class ScaleCommand
{
	public static void register(final CommandDispatcher<ServerCommandSource> commandDispatcher)
	{
		if (FMLLoader.isProduction() && !PehkuiConfig.COMMON.enableCommands.get())
		{
			return;
		}
		
		final LiteralArgumentBuilder<ServerCommandSource> builder =
			CommandManager.literal("scale")
			.requires(commandSource ->
			{
				return commandSource.hasPermissionLevel(2);
			});
		
		registerOperation(builder);
		registerRandomize(builder);
		registerGet(builder);
		registerCompute(builder);
		registerReset(builder);
		registerModifier(builder);
		registerDelay(builder);
		registerEasing(builder);
		registerPersist(builder);
		registerNbt(builder);
		
		commandDispatcher.register(builder);
	}
	
	private static LiteralArgumentBuilder<ServerCommandSource> registerOperation(final LiteralArgumentBuilder<ServerCommandSource> builder)
	{
		builder
			.then(CommandManager.argument("operation", ScaleOperationArgumentType.operation())
				.then(CommandManager.argument("scale_type", ScaleTypeArgumentType.scaleType())
					.then(CommandManager.argument("value", FloatArgumentType.floatArg())
						.then(CommandManager.argument("targets", EntityArgumentType.entities())
							.executes(context ->
							{
								final float scale = FloatArgumentType.getFloat(context, "value");
								final ScaleType type = ScaleTypeArgumentType.getScaleTypeArgument(context, "scale_type");
								
								for (final Entity e : EntityArgumentType.getEntities(context, "targets"))
								{
									final ScaleData data = type.getScaleData(e);
									final ScaleOperationArgumentType.Operation operation = ScaleOperationArgumentType.getOperation(context, "operation");
									
									data.setTargetScale((float) operation.apply(data.getTargetScale(), scale));
								}
								
								return 1;
							})
						)
						.executes(context ->
						{
							final float scale = FloatArgumentType.getFloat(context, "value");
							final ScaleType type = ScaleTypeArgumentType.getScaleTypeArgument(context, "scale_type");
							
							final ScaleData data = type.getScaleData(context.getSource().getEntityOrThrow());
							final ScaleOperationArgumentType.Operation operation = ScaleOperationArgumentType.getOperation(context, "operation");
							
							data.setTargetScale((float) operation.apply(data.getTargetScale(), scale));
							
							return 1;
						})
					)
				)
				.then(CommandManager.argument("value", FloatArgumentType.floatArg())
					.then(CommandManager.argument("targets", EntityArgumentType.entities())
						.executes(context ->
						{
							final float scale = FloatArgumentType.getFloat(context, "value");
							
							for (final Entity e : EntityArgumentType.getEntities(context, "targets"))
							{
								final ScaleData data = ScaleTypes.BASE.getScaleData(e);
								final ScaleOperationArgumentType.Operation operation = ScaleOperationArgumentType.getOperation(context, "operation");
								
								data.setTargetScale((float) operation.apply(data.getTargetScale(), scale));
							}
							
							return 1;
						})
					)
					.executes(context ->
					{
						final float scale = FloatArgumentType.getFloat(context, "value");
						
						final ScaleData data = ScaleTypes.BASE.getScaleData(context.getSource().getEntityOrThrow());
						final ScaleOperationArgumentType.Operation operation = ScaleOperationArgumentType.getOperation(context, "operation");
						
						data.setTargetScale((float) operation.apply(data.getTargetScale(), scale));
						
						return 1;
					})
				)
			);
		
		return builder;
	}
	
	private static LiteralArgumentBuilder<ServerCommandSource> registerRandomize(final LiteralArgumentBuilder<ServerCommandSource> builder)
	{
		builder
			.then(CommandManager.literal("randomize")
				.then(CommandManager.argument("scale_type", ScaleTypeArgumentType.scaleType())
					.then(CommandManager.argument("minOperation", ScaleOperationArgumentType.operation())
						.then(CommandManager.argument("minValue", FloatArgumentType.floatArg())
							.then(CommandManager.argument("maxOperation", ScaleOperationArgumentType.operation())
								.then(CommandManager.argument("maxValue", FloatArgumentType.floatArg())
									.then(CommandManager.argument("targets", EntityArgumentType.entities())
										.executes(context ->
										{
											final float minValue = FloatArgumentType.getFloat(context, "minValue");
											final float maxValue = FloatArgumentType.getFloat(context, "maxValue");
											
											final ScaleOperationArgumentType.Operation minOperation = ScaleOperationArgumentType.getOperation(context, "minOperation");
											final ScaleOperationArgumentType.Operation maxOperation = ScaleOperationArgumentType.getOperation(context, "maxOperation");
											
											final ScaleType type = ScaleTypeArgumentType.getScaleTypeArgument(context, "scale_type");
											
											double min, max, target;
											for (final Entity e : EntityArgumentType.getEntities(context, "targets"))
											{
												final ScaleData data = type.getScaleData(e);
												
												target = data.getTargetScale();
												min = minOperation.apply(target, minValue);
												max = maxOperation.apply(target, maxValue);
												
												if (max < min)
												{
													final double temp = min;
													min = max;
													max = temp;
												}
												
												data.setTargetScale((float) (min + (RANDOM.nextFloat() * (max - min))));
											}
											
											return 1;
										})
									)
									.executes(context ->
									{
										final float minValue = FloatArgumentType.getFloat(context, "minValue");
										final float maxValue = FloatArgumentType.getFloat(context, "maxValue");
										
										final ScaleOperationArgumentType.Operation minOperation = ScaleOperationArgumentType.getOperation(context, "minOperation");
										final ScaleOperationArgumentType.Operation maxOperation = ScaleOperationArgumentType.getOperation(context, "maxOperation");
										
										final ScaleType type = ScaleTypeArgumentType.getScaleTypeArgument(context, "scale_type");
										
										final ScaleData data = type.getScaleData(context.getSource().getEntityOrThrow());
										
										final double target = data.getTargetScale();
										double min = minOperation.apply(target, minValue);
										double max = maxOperation.apply(target, maxValue);
										
										if (max < min)
										{
											final double temp = min;
											min = max;
											max = temp;
										}
										
										data.setTargetScale((float) (min + (RANDOM.nextFloat() * (max - min))));
										
										return 1;
									})
								)
							)
						)
					)
				)
			);
		
		return builder;
	}
	
	private static LiteralArgumentBuilder<ServerCommandSource> registerGet(final LiteralArgumentBuilder<ServerCommandSource> builder)
	{
		builder
			.then(CommandManager.literal("get")
				.then(CommandManager.argument("scale_type", ScaleTypeArgumentType.scaleType())
					.then(CommandManager.argument("entity", EntityArgumentType.entity())
						.then(CommandManager.argument("scalingFactor", FloatArgumentType.floatArg())
							.executes(context ->
							{
								final ScaleType type = ScaleTypeArgumentType.getScaleTypeArgument(context, "scale_type");
								final float scale = type.getScaleData(EntityArgumentType.getEntity(context, "entity")).getBaseScale();
								final int scaled = (int) (scale * FloatArgumentType.getFloat(context, "scalingFactor"));
								CommandUtils.sendFeedback(context.getSource(), () -> scaleText(scale, scaled), false);
								
								return scaled;
							})
						)
						.executes(context ->
						{
							final ScaleType type = ScaleTypeArgumentType.getScaleTypeArgument(context, "scale_type");
							final float scale = type.getScaleData(EntityArgumentType.getEntity(context, "entity")).getBaseScale();
							CommandUtils.sendFeedback(context.getSource(), () -> scaleText(scale), false);
							
							return (int) scale;
						})
					)
					.then(CommandManager.argument("scalingFactor", FloatArgumentType.floatArg())
						.executes(context ->
						{
							final ScaleType type = ScaleTypeArgumentType.getScaleTypeArgument(context, "scale_type");
							final float scale = type.getScaleData(context.getSource().getEntityOrThrow()).getBaseScale();
							final int scaled = (int) (scale * FloatArgumentType.getFloat(context, "scalingFactor"));
							CommandUtils.sendFeedback(context.getSource(), () -> scaleText(scale, scaled), false);
							
							return scaled;
						})
					)
					.executes(context ->
					{
						final ScaleType type = ScaleTypeArgumentType.getScaleTypeArgument(context, "scale_type");
						final float scale = type.getScaleData(context.getSource().getEntityOrThrow()).getBaseScale();
						CommandUtils.sendFeedback(context.getSource(), () -> scaleText(scale), false);
						
						return (int) scale;
					})
				)
				.then(CommandManager.argument("scalingFactor", FloatArgumentType.floatArg())
					.executes(context ->
					{
						final float scale = ScaleTypes.BASE.getScaleData(context.getSource().getEntityOrThrow()).getBaseScale();
						final int scaled = (int) (scale * FloatArgumentType.getFloat(context, "scalingFactor"));
						CommandUtils.sendFeedback(context.getSource(), () -> scaleText(scale, scaled), false);
						
						return scaled;
					})
				)
				.then(CommandManager.argument("entity", EntityArgumentType.entity())
					.then(CommandManager.argument("scalingFactor", FloatArgumentType.floatArg())
						.executes(context ->
						{
							final float scale = ScaleTypes.BASE.getScaleData(EntityArgumentType.getEntity(context, "entity")).getBaseScale();
							final int scaled = (int) (scale * FloatArgumentType.getFloat(context, "scalingFactor"));
							CommandUtils.sendFeedback(context.getSource(), () -> scaleText(scale, scaled), false);
							
							return scaled;
						})
					)
					.executes(context ->
					{
						final float scale = ScaleTypes.BASE.getScaleData(EntityArgumentType.getEntity(context, "entity")).getBaseScale();
						CommandUtils.sendFeedback(context.getSource(), () -> scaleText(scale), false);
						
						return (int) scale;
					})
				)
				.executes(context ->
				{
					final float scale = ScaleTypes.BASE.getScaleData(context.getSource().getEntityOrThrow()).getBaseScale();
					CommandUtils.sendFeedback(context.getSource(), () -> scaleText(scale), false);
					
					return (int) scale;
				})
			);
		
		return builder;
	}
	
	private static LiteralArgumentBuilder<ServerCommandSource> registerCompute(final LiteralArgumentBuilder<ServerCommandSource> builder)
	{
		builder
			.then(CommandManager.literal("compute")
				.then(CommandManager.argument("scale_type", ScaleTypeArgumentType.scaleType())
					.then(CommandManager.argument("entity", EntityArgumentType.entity())
						.then(CommandManager.argument("scalingFactor", FloatArgumentType.floatArg())
							.executes(context ->
							{
								final ScaleType type = ScaleTypeArgumentType.getScaleTypeArgument(context, "scale_type");
								final float scale = type.getScaleData(EntityArgumentType.getEntity(context, "entity")).getScale();
								final int scaled = (int) (scale * FloatArgumentType.getFloat(context, "scalingFactor"));
								CommandUtils.sendFeedback(context.getSource(), () -> scaleText(scale, scaled), false);
								
								return scaled;
							})
						)
						.executes(context ->
						{
							final ScaleType type = ScaleTypeArgumentType.getScaleTypeArgument(context, "scale_type");
							final float scale = type.getScaleData(EntityArgumentType.getEntity(context, "entity")).getScale();
							CommandUtils.sendFeedback(context.getSource(), () -> scaleText(scale), false);
							
							return (int) scale;
						})
					)
					.then(CommandManager.argument("scalingFactor", FloatArgumentType.floatArg())
						.executes(context ->
						{
							final ScaleType type = ScaleTypeArgumentType.getScaleTypeArgument(context, "scale_type");
							final float scale = type.getScaleData(context.getSource().getEntityOrThrow()).getScale();
							final int scaled = (int) (scale * FloatArgumentType.getFloat(context, "scalingFactor"));
							CommandUtils.sendFeedback(context.getSource(), () -> scaleText(scale, scaled), false);
							
							return scaled;
						})
					)
					.executes(context ->
					{
						final ScaleType type = ScaleTypeArgumentType.getScaleTypeArgument(context, "scale_type");
						final float scale = type.getScaleData(context.getSource().getEntityOrThrow()).getScale();
						CommandUtils.sendFeedback(context.getSource(), () -> scaleText(scale), false);
						
						return (int) scale;
					})
				)
			);
		
		return builder;
	}
	
	private static LiteralArgumentBuilder<ServerCommandSource> registerReset(final LiteralArgumentBuilder<ServerCommandSource> builder)
	{
		builder
			.then(CommandManager.literal("reset")
				.then(CommandManager.argument("scale_type", ScaleTypeArgumentType.scaleType())
					.then(CommandManager.argument("targets", EntityArgumentType.entities())
						.executes(context ->
						{
							for (final Entity e : EntityArgumentType.getEntities(context, "targets"))
							{
								final ScaleType type = ScaleTypeArgumentType.getScaleTypeArgument(context, "scale_type");
								final ScaleData data = type.getScaleData(e);
								final Boolean persist = data.getPersistence();
								data.resetScale();
								data.setPersistence(persist);
							}
							
							return 1;
						})
					)
					.executes(context ->
					{
						final ScaleType type = ScaleTypeArgumentType.getScaleTypeArgument(context, "scale_type");
						final ScaleData data = type.getScaleData(context.getSource().getEntityOrThrow());
						final Boolean persist = data.getPersistence();
						data.resetScale();
						data.setPersistence(persist);
						
						return 1;
					})
				)
				.then(CommandManager.argument("targets", EntityArgumentType.entities())
					.executes(context ->
					{
						for (final Entity e : EntityArgumentType.getEntities(context, "targets"))
						{
							for (final ScaleType type : ScaleRegistries.SCALE_TYPES.values())
							{
								final ScaleData data = type.getScaleData(e);
								final Boolean persist = data.getPersistence();
								data.resetScale();
								data.setPersistence(persist);
							}
						}
						
						return 1;
					})
				)
				.executes(context ->
				{
					for (final ScaleType type : ScaleRegistries.SCALE_TYPES.values())
					{
						final ScaleData data = type.getScaleData(context.getSource().getEntityOrThrow());
						final Boolean persist = data.getPersistence();
						data.resetScale();
						data.setPersistence(persist);
					}
					
					return 1;
				})
			);
		
		return builder;
	}
	
	private static LiteralArgumentBuilder<ServerCommandSource> registerModifier(final LiteralArgumentBuilder<ServerCommandSource> builder)
	{
		builder
			.then(CommandManager.literal("modifier")
				.then(CommandManager.literal("get")
					.then(CommandManager.argument("scale_type", ScaleTypeArgumentType.scaleType())
						.then(CommandManager.argument("entity", EntityArgumentType.entity())
							.executes(context ->
							{
								final ScaleType type = ScaleTypeArgumentType.getScaleTypeArgument(context, "scale_type");
								final ScaleData data = type.getScaleData(EntityArgumentType.getEntity(context, "entity"));
								
								final String modifierString = String.join(", ", data.getBaseValueModifiers().stream().map(e -> ScaleRegistries.getId(ScaleRegistries.SCALE_MODIFIERS, e).toString()).collect(Collectors.toList()));
								
								CommandUtils.sendFeedback(context.getSource(), () -> modifierText(modifierString), false);
								
								return 1;
							})
						)
						.executes(context ->
						{
							final ScaleType type = ScaleTypeArgumentType.getScaleTypeArgument(context, "scale_type");
							
							final String modifierString = String.join(", ", type.getDefaultBaseValueModifiers().stream().map(e -> ScaleRegistries.getId(ScaleRegistries.SCALE_MODIFIERS, e).toString()).collect(Collectors.toList()));
							
							CommandUtils.sendFeedback(context.getSource(), () -> modifierText(modifierString), false);
							
							return 1;
						})
					)
				)
				.then(CommandManager.literal("add")
					.then(CommandManager.argument("scale_type", ScaleTypeArgumentType.scaleType())
						.then(CommandManager.argument("scale_modifier", ScaleModifierArgumentType.scaleModifier())
							.then(CommandManager.argument("targets", EntityArgumentType.entities())
								.executes(context ->
								{
									for (final Entity e : EntityArgumentType.getEntities(context, "targets"))
									{
										final ScaleType type = ScaleTypeArgumentType.getScaleTypeArgument(context, "scale_type");
										final ScaleModifier modifier = ScaleModifierArgumentType.getScaleModifierArgument(context, "scale_modifier");
										final ScaleData data = type.getScaleData(e);
										data.getBaseValueModifiers().add(modifier);
									}
									
									return 1;
								})
							)
							.executes(context ->
							{
								final ScaleType type = ScaleTypeArgumentType.getScaleTypeArgument(context, "scale_type");
								final ScaleModifier modifier = ScaleModifierArgumentType.getScaleModifierArgument(context, "scale_modifier");
								final ScaleData data = type.getScaleData(context.getSource().getEntityOrThrow());
								data.getBaseValueModifiers().add(modifier);
								
								return 1;
							})
						)
					)
				)
				.then(CommandManager.literal("remove")
					.then(CommandManager.argument("scale_type", ScaleTypeArgumentType.scaleType())
						.then(CommandManager.argument("scale_modifier", ScaleModifierArgumentType.scaleModifier())
							.then(CommandManager.argument("targets", EntityArgumentType.entities())
								.executes(context ->
								{
									for (final Entity e : EntityArgumentType.getEntities(context, "targets"))
									{
										final ScaleType type = ScaleTypeArgumentType.getScaleTypeArgument(context, "scale_type");
										final ScaleModifier modifier = ScaleModifierArgumentType.getScaleModifierArgument(context, "scale_modifier");
										final ScaleData data = type.getScaleData(e);
										data.getBaseValueModifiers().remove(modifier);
									}
									
									return 1;
								})
							)
							.executes(context ->
							{
								final ScaleType type = ScaleTypeArgumentType.getScaleTypeArgument(context, "scale_type");
								final ScaleModifier modifier = ScaleModifierArgumentType.getScaleModifierArgument(context, "scale_modifier");
								final ScaleData data = type.getScaleData(context.getSource().getEntityOrThrow());
								data.getBaseValueModifiers().remove(modifier);
								
								return 1;
							})
						)
					)
				)
				.then(CommandManager.literal("reset")
					.then(CommandManager.argument("scale_type", ScaleTypeArgumentType.scaleType())
						.then(CommandManager.argument("targets", EntityArgumentType.entities())
							.executes(context ->
							{
								for (final Entity e : EntityArgumentType.getEntities(context, "targets"))
								{
									final ScaleType type = ScaleTypeArgumentType.getScaleTypeArgument(context, "scale_type");
									final ScaleData data = type.getScaleData(e);
									
									final SortedSet<ScaleModifier> baseValueModifiers = data.getBaseValueModifiers();
									
									baseValueModifiers.clear();
									baseValueModifiers.addAll(type.getDefaultBaseValueModifiers());
								}
								
								return 1;
							})
						)
						.executes(context ->
						{
							final ScaleType type = ScaleTypeArgumentType.getScaleTypeArgument(context, "scale_type");
							final ScaleData data = type.getScaleData(context.getSource().getEntityOrThrow());
							
							final SortedSet<ScaleModifier> baseValueModifiers = data.getBaseValueModifiers();
							
							baseValueModifiers.clear();
							baseValueModifiers.addAll(type.getDefaultBaseValueModifiers());
							
							return 1;
						})
					)
				)
			);
		
		return builder;
	}
	
	private static LiteralArgumentBuilder<ServerCommandSource> registerDelay(final LiteralArgumentBuilder<ServerCommandSource> builder)
	{
		builder
			.then(CommandManager.literal("delay")
				.then(CommandManager.literal("set")
					.then(CommandManager.argument("scale_type", ScaleTypeArgumentType.scaleType())
						.then(CommandManager.argument("ticks", IntegerArgumentType.integer())
							.then(CommandManager.argument("targets", EntityArgumentType.entities())
								.executes(context ->
								{
									final int ticks = IntegerArgumentType.getInteger(context, "ticks");
									final ScaleType type = ScaleTypeArgumentType.getScaleTypeArgument(context, "scale_type");
									
									for (final Entity e : EntityArgumentType.getEntities(context, "targets"))
									{
										final ScaleData data = type.getScaleData(e);
										
										data.setScaleTickDelay(ticks);
									}
									
									return 1;
								})
							)
							.executes(context ->
							{
								final int ticks = IntegerArgumentType.getInteger(context, "ticks");
								final ScaleType type = ScaleTypeArgumentType.getScaleTypeArgument(context, "scale_type");
								
								final ScaleData data = type.getScaleData(context.getSource().getEntityOrThrow());
								
								data.setScaleTickDelay(ticks);
								
								return 1;
							})
						)
					)
					.then(CommandManager.argument("ticks", IntegerArgumentType.integer())
						.then(CommandManager.argument("targets", EntityArgumentType.entities())
							.executes(context ->
							{
								final int ticks = IntegerArgumentType.getInteger(context, "ticks");
								
								for (final Entity e : EntityArgumentType.getEntities(context, "targets"))
								{
									final ScaleData data = ScaleTypes.BASE.getScaleData(e);
									
									data.setScaleTickDelay(ticks);
								}
								
								return 1;
							})
						)
						.executes(context ->
						{
							final int ticks = IntegerArgumentType.getInteger(context, "ticks");
							
							final ScaleData data = ScaleTypes.BASE.getScaleData(context.getSource().getEntityOrThrow());
							
							data.setScaleTickDelay(ticks);
							
							return 1;
						})
					)
				)
				.then(CommandManager.literal("get")
					.then(CommandManager.argument("scale_type", ScaleTypeArgumentType.scaleType())
						.then(CommandManager.argument("entity", EntityArgumentType.entity())
							.executes(context ->
							{
								final ScaleType type = ScaleTypeArgumentType.getScaleTypeArgument(context, "scale_type");
								final int ticks = type.getScaleData(EntityArgumentType.getEntity(context, "entity")).getScaleTickDelay();
								CommandUtils.sendFeedback(context.getSource(), () -> delayText(ticks), false);
								return 1;
							})
						)
						.executes(context ->
						{
							final ScaleType type = ScaleTypeArgumentType.getScaleTypeArgument(context, "scale_type");
							final int ticks = type.getScaleData(context.getSource().getEntityOrThrow()).getScaleTickDelay();
							CommandUtils.sendFeedback(context.getSource(), () -> delayText(ticks), false);
							return 1;
						})
					)
					.then(CommandManager.argument("entity", EntityArgumentType.entity())
						.executes(context ->
						{
							final int ticks = ScaleTypes.BASE.getScaleData(EntityArgumentType.getEntity(context, "entity")).getScaleTickDelay();
							CommandUtils.sendFeedback(context.getSource(), () -> delayText(ticks), false);
							return 1;
						})
					)
					.executes(context ->
					{
						final int ticks = ScaleTypes.BASE.getScaleData(context.getSource().getEntityOrThrow()).getScaleTickDelay();
						CommandUtils.sendFeedback(context.getSource(), () -> delayText(ticks), false);
						return 1;
					})
				)
				.then(CommandManager.literal("reset")
					.then(CommandManager.argument("scale_type", ScaleTypeArgumentType.scaleType())
						.then(CommandManager.argument("targets", EntityArgumentType.entities())
							.executes(context ->
							{
								final ScaleType type = ScaleTypeArgumentType.getScaleTypeArgument(context, "scale_type");
								final int ticks = type.getDefaultTickDelay();
								
								for (final Entity e : EntityArgumentType.getEntities(context, "targets"))
								{
									final ScaleData data = type.getScaleData(e);
									
									data.setScaleTickDelay(ticks);
								}
								
								CommandUtils.sendFeedback(context.getSource(), () -> delayText(ticks), false);
								
								return 1;
							})
						)
						.executes(context ->
						{
							final ScaleType type = ScaleTypeArgumentType.getScaleTypeArgument(context, "scale_type");
							final int ticks = type.getDefaultTickDelay();
							
							final ScaleData data = type.getScaleData(context.getSource().getEntityOrThrow());
							
							data.setScaleTickDelay(ticks);
							
							CommandUtils.sendFeedback(context.getSource(), () -> delayText(ticks), false);
							
							return 1;
						})
					)
					.then(CommandManager.argument("targets", EntityArgumentType.entities())
						.executes(context ->
						{
							final int ticks = ScaleTypes.BASE.getDefaultTickDelay();
							
							for (final Entity e : EntityArgumentType.getEntities(context, "targets"))
							{
								final ScaleData data = ScaleTypes.BASE.getScaleData(e);
								
								data.setScaleTickDelay(ticks);
							}
							
							CommandUtils.sendFeedback(context.getSource(), () -> delayText(ticks), false);
							
							return 1;
						})
					)
					.executes(context ->
					{
						final int ticks = ScaleTypes.BASE.getDefaultTickDelay();
						
						final ScaleData data = ScaleTypes.BASE.getScaleData(context.getSource().getEntityOrThrow());
						
						data.setScaleTickDelay(ticks);
						
						CommandUtils.sendFeedback(context.getSource(), () -> delayText(ticks), false);
						
						return 1;
					})
				)
			);
		
		return builder;
	}
	
	private static LiteralArgumentBuilder<ServerCommandSource> registerEasing(final LiteralArgumentBuilder<ServerCommandSource> builder)
	{
		builder
			.then(CommandManager.literal("easing")
				.then(CommandManager.literal("set")
					.then(CommandManager.argument("scale_type", ScaleTypeArgumentType.scaleType())
						.then(CommandManager.argument("easing", ScaleEasingArgumentType.scaleEasing())
							.then(CommandManager.argument("targets", EntityArgumentType.entities())
								.executes(context ->
								{
									final Float2FloatFunction easing = ScaleEasingArgumentType.getScaleEasingArgument(context, "easing");
									final ScaleType type = ScaleTypeArgumentType.getScaleTypeArgument(context, "scale_type");
									
									for (final Entity e : EntityArgumentType.getEntities(context, "targets"))
									{
										final ScaleData data = type.getScaleData(e);
										
										data.setEasing(easing);
									}
									
									return 1;
								})
							)
							.executes(context ->
							{
								final Float2FloatFunction easing = ScaleEasingArgumentType.getScaleEasingArgument(context, "easing");
								final ScaleType type = ScaleTypeArgumentType.getScaleTypeArgument(context, "scale_type");
								
								final ScaleData data = type.getScaleData(context.getSource().getEntityOrThrow());
								
								data.setEasing(easing);
								
								return 1;
							})
						)
					)
				)
				.then(CommandManager.literal("get")
					.then(CommandManager.argument("scale_type", ScaleTypeArgumentType.scaleType())
						.then(CommandManager.argument("entity", EntityArgumentType.entity())
							.executes(context ->
							{
								final ScaleType type = ScaleTypeArgumentType.getScaleTypeArgument(context, "scale_type");
								final Float2FloatFunction easing = type.getScaleData(EntityArgumentType.getEntity(context, "entity")).getEasing();
								CommandUtils.sendFeedback(context.getSource(), () -> easingText(easing, type), false);
								return 1;
							})
						)
						.executes(context ->
						{
							final ScaleType type = ScaleTypeArgumentType.getScaleTypeArgument(context, "scale_type");
							final Float2FloatFunction easing = type.getScaleData(context.getSource().getEntityOrThrow()).getEasing();
							CommandUtils.sendFeedback(context.getSource(), () -> easingText(easing, type), false);
							return 1;
						})
					)
					.then(CommandManager.argument("entity", EntityArgumentType.entity())
						.executes(context ->
						{
							final Float2FloatFunction easing = ScaleTypes.BASE.getScaleData(EntityArgumentType.getEntity(context, "entity")).getEasing();
							CommandUtils.sendFeedback(context.getSource(), () -> easingText(easing, ScaleTypes.BASE), false);
							return 1;
						})
					)
					.executes(context ->
					{
						final Float2FloatFunction easing = ScaleTypes.BASE.getScaleData(context.getSource().getEntityOrThrow()).getEasing();
						CommandUtils.sendFeedback(context.getSource(), () -> easingText(easing, ScaleTypes.BASE), false);
						return 1;
					})
				)
				.then(CommandManager.literal("reset")
					.then(CommandManager.argument("scale_type", ScaleTypeArgumentType.scaleType())
						.then(CommandManager.argument("targets", EntityArgumentType.entities())
							.executes(context ->
							{
								final ScaleType type = ScaleTypeArgumentType.getScaleTypeArgument(context, "scale_type");
								
								for (final Entity e : EntityArgumentType.getEntities(context, "targets"))
								{
									final ScaleData data = type.getScaleData(e);
									
									data.setEasing(null);
								}
								
								CommandUtils.sendFeedback(context.getSource(), () -> easingText(null, type), false);
								
								return 1;
							})
						)
						.executes(context ->
						{
							final ScaleType type = ScaleTypeArgumentType.getScaleTypeArgument(context, "scale_type");
							
							final ScaleData data = type.getScaleData(context.getSource().getEntityOrThrow());
							
							data.setEasing(null);
							
							CommandUtils.sendFeedback(context.getSource(), () -> easingText(null, type), false);
							
							return 1;
						})
					)
					.then(CommandManager.argument("targets", EntityArgumentType.entities())
						.executes(context ->
						{
							for (final Entity e : EntityArgumentType.getEntities(context, "targets"))
							{
								final ScaleData data = ScaleTypes.BASE.getScaleData(e);
								
								data.setEasing(null);
							}
							
							CommandUtils.sendFeedback(context.getSource(), () -> easingText(null, ScaleTypes.BASE), false);
							
							return 1;
						})
					)
					.executes(context ->
					{
						final ScaleData data = ScaleTypes.BASE.getScaleData(context.getSource().getEntityOrThrow());
						
						data.setEasing(null);
						
						CommandUtils.sendFeedback(context.getSource(), () -> easingText(null, ScaleTypes.BASE), false);
						
						return 1;
					})
				)
			);
		
		return builder;
	}
	
	private static LiteralArgumentBuilder<ServerCommandSource> registerPersist(final LiteralArgumentBuilder<ServerCommandSource> builder)
	{
		builder
			.then(CommandManager.literal("persist")
				.then(CommandManager.literal("set")
					.then(CommandManager.argument("scale_type", ScaleTypeArgumentType.scaleType())
						.then(CommandManager.argument("enabled", BoolArgumentType.bool())
							.then(CommandManager.argument("targets", EntityArgumentType.entities())
								.executes(context ->
								{
									final boolean persist = BoolArgumentType.getBool(context, "enabled");
									final ScaleType type = ScaleTypeArgumentType.getScaleTypeArgument(context, "scale_type");
									
									for (final Entity e : EntityArgumentType.getEntities(context, "targets"))
									{
										final ScaleData data = type.getScaleData(e);
										
										data.setPersistence(persist);
									}
									
									return 1;
								})
							)
							.executes(context ->
							{
								final boolean persist = BoolArgumentType.getBool(context, "enabled");
								final ScaleType type = ScaleTypeArgumentType.getScaleTypeArgument(context, "scale_type");
								
								final ScaleData data = type.getScaleData(context.getSource().getEntityOrThrow());
								
								data.setPersistence(persist);
								
								return 1;
							})
						)
					)
					.then(CommandManager.argument("enabled", BoolArgumentType.bool())
						.then(CommandManager.argument("targets", EntityArgumentType.entities())
							.executes(context ->
							{
								final boolean persist = BoolArgumentType.getBool(context, "enabled");
								
								for (final Entity e : EntityArgumentType.getEntities(context, "targets"))
								{
									for (final ScaleType type : ScaleRegistries.SCALE_TYPES.values())
									{
										final ScaleData data = type.getScaleData(e);
										data.setPersistence(persist);
									}
								}
								
								return 1;
							})
						)
						.executes(context ->
						{
							final boolean persist = BoolArgumentType.getBool(context, "enabled");
							
							for (final ScaleType type : ScaleRegistries.SCALE_TYPES.values())
							{
								final ScaleData data = type.getScaleData(context.getSource().getEntityOrThrow());
								data.setPersistence(persist);
							}
							
							return 1;
						})
					)
				)
				.then(CommandManager.literal("get")
					.then(CommandManager.argument("scale_type", ScaleTypeArgumentType.scaleType())
						.then(CommandManager.argument("entity", EntityArgumentType.entity())
							.executes(context ->
							{
								final ScaleType type = ScaleTypeArgumentType.getScaleTypeArgument(context, "scale_type");
								final Boolean persist = type.getScaleData(EntityArgumentType.getEntity(context, "entity")).getPersistence();
								CommandUtils.sendFeedback(context.getSource(), () -> persistenceText(persist, type), false);
								return 1;
							})
						)
						.executes(context ->
						{
							final ScaleType type = ScaleTypeArgumentType.getScaleTypeArgument(context, "scale_type");
							final Boolean persist = type.getScaleData(context.getSource().getEntityOrThrow()).getPersistence();
							CommandUtils.sendFeedback(context.getSource(), () -> persistenceText(persist, type), false);
							return 1;
						})
					)
				)
				.then(CommandManager.literal("reset")
					.then(CommandManager.argument("scale_type", ScaleTypeArgumentType.scaleType())
						.then(CommandManager.argument("targets", EntityArgumentType.entities())
							.executes(context ->
							{
								final ScaleType type = ScaleTypeArgumentType.getScaleTypeArgument(context, "scale_type");
								
								for (final Entity e : EntityArgumentType.getEntities(context, "targets"))
								{
									final ScaleData data = type.getScaleData(e);
									
									data.setPersistence(null);
								}
								
								return 1;
							})
						)
						.executes(context ->
						{
							final ScaleType type = ScaleTypeArgumentType.getScaleTypeArgument(context, "scale_type");
							
							final ScaleData data = type.getScaleData(context.getSource().getEntityOrThrow());
							
							data.setPersistence(null);
							
							return 1;
						})
					)
					.then(CommandManager.argument("targets", EntityArgumentType.entities())
						.executes(context ->
						{
							for (final Entity e : EntityArgumentType.getEntities(context, "targets"))
							{
								for (final ScaleType type : ScaleRegistries.SCALE_TYPES.values())
								{
									final ScaleData data = type.getScaleData(e);
									data.setPersistence(null);
								}
							}
							
							return 1;
						})
					)
					.executes(context ->
					{
						for (final ScaleType type : ScaleRegistries.SCALE_TYPES.values())
						{
							final ScaleData data = type.getScaleData(context.getSource().getEntityOrThrow());
							data.setPersistence(null);
						}
						
						return 1;
					})
				)
			);
		
		return builder;
	}
	
	private static LiteralArgumentBuilder<ServerCommandSource> registerNbt(final LiteralArgumentBuilder<ServerCommandSource> builder)
	{
		builder
			.then(CommandManager.literal("nbt")
				.then(CommandManager.literal("get")
					.then(CommandManager.argument("entity", EntityArgumentType.entity())
						.then(CommandManager.argument("path", NbtPathArgumentType.nbtPath())
							.executes(context ->
								DataCommandInvoker.Path.callExecuteGet(
									context.getSource(),
									new EntityScaleDataObject(EntityArgumentType.getEntity(context, "entity")),
									NbtPathArgumentType.getNbtPath(context, "path")
								)
							)
							.then(CommandManager.argument("scale", DoubleArgumentType.doubleArg())
								.executes(context ->
									DataCommandInvoker.Scaled.callExecuteGet(
										context.getSource(),
										new EntityScaleDataObject(EntityArgumentType.getEntity(context, "entity")),
										NbtPathArgumentType.getNbtPath(context, "path"),
										DoubleArgumentType.getDouble(context, "scale")
									)
								)
							)
						)
						.executes(context ->
							DataCommandInvoker.Get.callExecuteGet(
								context.getSource(),
								new EntityScaleDataObject(EntityArgumentType.getEntity(context, "entity"))
							)
						)
					)
					.executes(context ->
					{
						final EntityDataObject obj = new EntityScaleDataObject(context.getSource().getEntityOrThrow());
						
						final NbtCompound nbt = obj.getNbt();
						CommandUtils.sendFeedback(context.getSource(), () -> obj.feedbackQuery(nbt), false);
						
						return nbt.getSize();
					})
				)
			);
		
		return builder;
	}
	
	private static class EntityScaleDataObject extends EntityDataObject
	{
		private final Entity entity;
		
		public EntityScaleDataObject(Entity entity)
		{
			super(entity);
			this.entity = entity;
		}
		
		@Override
		public void setNbt(NbtCompound nbt) throws CommandSyntaxException
		{
			((PehkuiEntityExtensions) entity).pehkui_readScaleNbt(nbt);
		}
		
		@Override
		public NbtCompound getNbt()
		{
			return ((PehkuiEntityExtensions) entity).pehkui_writeScaleNbt(new NbtCompound());
		}
	}
	
	private static Text scaleText(float scale)
	{
		final long denominator = (long) (1.0F / scale);
		if (((long) scale) != 1L && Float.compare(scale, 1.0F / denominator) == 0)
		{
			return I18nUtils.translate("commands.pehkui.scale.get.fraction.message", "Scale: %s (1/%s)", format(scale), format(denominator));
		}
		
		return I18nUtils.translate("commands.pehkui.scale.get.message", "Scale: %s", format(scale));
	}
	
	private static Text scaleText(float scale, int multiplied)
	{
		final long denominator = (long) (1.0F / scale);
		if (((long) scale) != 1L && Float.compare(scale, 1.0F / denominator) == 0)
		{
			return I18nUtils.translate("commands.pehkui.scale.get.fraction.factor.message", "Scale: %s (1/%s) | (%s)", format(scale), format(denominator), format(multiplied));
		}
		
		return I18nUtils.translate("commands.pehkui.scale.get.factor.message", "Scale: %s | (%s)", format(scale), format(multiplied));
	}
	
	private static Text modifierText(String modifierString)
	{
		return I18nUtils.translate("commands.pehkui.scale.modifier.get.message", "%s", modifierString.isEmpty() ? "N/A" : modifierString);
	}
	
	private static Text delayText(int ticks)
	{
		return I18nUtils.translate("commands.pehkui.scale.delay.get.message", "Delay: %s ticks", format(ticks));
	}
	
	private static Text persistenceText(@Nullable Boolean persist, ScaleType type)
	{
		final String unlocalized = "commands.pehkui.scale.persist." + (persist != null ? persist : ("default." + type.getDefaultPersistence()));
		final String message = "Persistent: " + (persist == null ? "default (" + type.getDefaultPersistence() + ")"  : persist);
		return I18nUtils.translate(unlocalized, message);
	}
	
	private static Text easingText(@Nullable Float2FloatFunction easing, ScaleType type)
	{
		final String easingId = ScaleRegistries.getId(ScaleRegistries.SCALE_EASINGS, easing != null ? easing : type.getDefaultEasing()).toString();
		final String unlocalized = "commands.pehkui.scale.easing" + (easing != null ? "" : ".default");
		final String message = "Easing: " + (easing == null ? "default (" + easingId + ")" : easingId);
		return I18nUtils.translate(unlocalized, message, easingId);
	}
	
	private static final Random RANDOM = new Random();
	
	private static final DecimalFormat SCALE_FORMAT;
	
	static
	{
		SCALE_FORMAT = new DecimalFormat("#,##0");
		SCALE_FORMAT.setMaximumFractionDigits(340);
	}
	
	private static String format(int scale)
	{
		return SCALE_FORMAT.format(scale);
	}
	
	private static String format(long scale)
	{
		return SCALE_FORMAT.format(scale);
	}
	
	private static String format(float scale)
	{
		return SCALE_FORMAT.format(scale);
	}
}
