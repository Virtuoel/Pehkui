package virtuoel.pehkui.server.command;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraftforge.fml.loading.FMLLoader;
import virtuoel.pehkui.api.PehkuiConfig;
import virtuoel.pehkui.util.NbtCompoundExtensions;

public class DebugCommand
{
	public static void register(final CommandDispatcher<ServerCommandSource> commandDispatcher, final boolean dedicated)
	{
		register(commandDispatcher);
	}
	
	public static void register(final CommandDispatcher<ServerCommandSource> commandDispatcher)
	{
		commandDispatcher.register(
			CommandManager.literal("scale")
			.requires(commandSource ->
			{
				return commandSource.hasPermissionLevel(2);
			})
			.then(CommandManager.literal("debug")
				.then(CommandManager.literal("delete_scale_data")
					.then(CommandManager.literal("uuid")
						.then(CommandManager.argument("uuid", StringArgumentType.string())
							.executes(context ->
							{
								final String uuidString = StringArgumentType.getString(context, "uuid");
								
								try
								{
									MARKED_UUIDS.add(UUID.fromString(uuidString));
								}
								catch (IllegalArgumentException e)
								{
									context.getSource().sendError(new LiteralText("Invalid UUID \"" + uuidString + "\"."));
									return 0;
								}
								
								return 1;
							})
						)
					)
					.then(CommandManager.literal("username")
						.then(CommandManager.argument("username", StringArgumentType.string())
							.executes(context ->
							{
								MARKED_USERNAMES.add(StringArgumentType.getString(context, "username").toLowerCase(Locale.ROOT));
								
								return 1;
							})
						)
					)
				)
				.then(CommandManager.literal("garbage_collect")
					.executes(context ->
					{
						System.gc();
						
						return 1;
					})
				)
			)
		);
		
		if (!FMLLoader.isProduction() || PehkuiConfig.COMMON.enableDebugCommands.get())
		{
			commandDispatcher.register(
				CommandManager.literal("scale")
				.requires(commandSource ->
				{
					return commandSource.hasPermissionLevel(2);
				})
				.then(CommandManager.literal("debug")
					.then(CommandManager.literal("run_mixin_tests")
						.executes(DebugCommand::runMixinTests)
					)
					.then(CommandManager.literal("run_tests")
						.executes(DebugCommand::runTests)
					)
				)
			);
		}
	}
	
	private static final Collection<UUID> MARKED_UUIDS = new HashSet<>();
	private static final Collection<String> MARKED_USERNAMES = new HashSet<>();
	
	public static boolean unmarkEntityForScaleReset(final Entity entity, final CompoundTag nbt)
	{
		if (entity instanceof PlayerEntity && MARKED_USERNAMES.remove(((PlayerEntity) entity).getGameProfile().getName().toLowerCase(Locale.ROOT)))
		{
			return true;
		}
		
		final NbtCompoundExtensions compound = ((NbtCompoundExtensions) nbt);
		
		return compound.pehkui_containsUuid("UUID") && MARKED_UUIDS.remove(compound.pehkui_getUuid("UUID"));
	}
	
	private static final List<EntityType<? extends Entity>> TYPES = Arrays.asList(
		EntityType.ZOMBIE,
		EntityType.CREEPER,
		EntityType.END_CRYSTAL,
		EntityType.BLAZE
	);
	
	private static int runTests(CommandContext<ServerCommandSource> context) throws CommandSyntaxException
	{
		ServerPlayerEntity player = context.getSource().getPlayer();
		
		Direction dir = player.getHorizontalFacing();
		Direction opposite = dir.getOpposite();
		
		Direction left = dir.rotateYCounterclockwise();
		Direction right = dir.rotateYClockwise();
		
		int distance = 4;
		int spacing = 2;
		
		int width = ((TYPES.size() - 1) * (spacing + 1)) + 1;
		
		BlockPos start = player.getBlockPos().offset(dir, distance).offset(left, width / 2);
		
		BlockPos.Mutable mut = start.mutableCopy();
		
		ServerWorld w = player.getServerWorld();
		
		for (EntityType<?> t : TYPES)
		{
			w.setBlockState(mut, Blocks.POLISHED_ANDESITE.getDefaultState());
			final Entity e = t.create(w);
			
			e.updatePositionAndAngles(mut.getX() + 0.5, mut.getY() + 1, mut.getZ() + 0.5, opposite.asRotation(), 0);
			e.refreshPositionAndAngles(mut.getX() + 0.5, mut.getY() + 1, mut.getZ() + 0.5, opposite.asRotation(), 0);
			e.setHeadYaw(opposite.asRotation());
			
			e.addScoreboardTag("pehkui");
			
			w.spawnEntity(e);
			
			mut.move(right, spacing + 1);
		}
		
		// TODO set command block w/ entity to void and block destroy under player pos
		
		context.getSource().sendFeedback(new LiteralText("Tests succeeded."), false);
		
		return 1;
	}
	
	private static int runMixinTests(CommandContext<ServerCommandSource> context) throws CommandSyntaxException
	{
		context.getSource().sendFeedback(new LiteralText("NYI"), false);
		
		return 1;
	}
}
