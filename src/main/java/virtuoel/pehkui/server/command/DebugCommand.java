package virtuoel.pehkui.server.command;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Blocks;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class DebugCommand
{
	private static final Collection<UUID> MARKED_UUIDS = new HashSet<>();
	
	public static boolean unmarkUuidForScaleReset(UUID uuid)
	{
		return MARKED_UUIDS.remove(uuid);
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
					.then(CommandManager.argument("uuid", UuidArgumentType.uuid())
						.executes(context ->
						{
							MARKED_UUIDS.add(UuidArgumentType.getUuid(context, "uuid"));
							
							return 1;
						})
					)
				)
			)
		);
		
		if (FabricLoader.getInstance().isDevelopmentEnvironment())
		{
			commandDispatcher.register(
				CommandManager.literal("scale")
				.requires(commandSource ->
				{
					return commandSource.hasPermissionLevel(2);
				})
				.then(CommandManager.literal("debug")
					.then(CommandManager.literal("run_tests")
						.executes(DebugCommand::runTests)
					)
				)
			);
		}
	}
}
