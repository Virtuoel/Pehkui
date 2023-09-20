package virtuoel.pehkui.util;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import virtuoel.kanos_config.api.JsonConfigHandler;
import virtuoel.kanos_config.api.MutableConfigEntry;
import virtuoel.pehkui.Pehkui;
import virtuoel.pehkui.api.PehkuiConfig;

public class ConfigSyncUtils
{
	public static final Map<String, MutableConfigEntry<?>> CONFIGS = new HashMap<>();
	private static final Map<String, SyncableConfigEntry<?>> SYNCED_CONFIGS = new HashMap<>();
	private static final Map<String, ConfigEntryCodec<?>> SYNCED_CONFIG_CODECS = new HashMap<>();
	private static final Map<String, ConfigEntryCodec<?>> CODECS = new HashMap<>();
	
	static
	{
		CODECS.put("double", new ConfigEntryCodec<Double>(
			(b, e) -> ((ByteBuf) b).writeDouble(e.getValue()),
			(b, e) ->
			{
				final double v = b.readDouble();
				
				return () -> e.setSyncedValue(v);
			},
			DoubleArgumentType::doubleArg,
			DoubleArgumentType::getDouble
		));
		CODECS.put("boolean", new ConfigEntryCodec<Boolean>(
			(b, e) -> ((ByteBuf) b).writeBoolean(e.getValue()),
			(b, e) ->
			{
				final boolean v = b.readBoolean();
				
				return () -> e.setSyncedValue(v);
			},
			BoolArgumentType::bool,
			BoolArgumentType::getBool
		));
		CODECS.put("string_list", new ConfigEntryCodec<List<String>>(
			(b, e) ->
			{
				final List<String> list = e.getValue();
				
				b.writeVarInt(list.size());
				for (final String v : list)
				{
					b.writeString(v);
				}
			},
			(b, e) ->
			{
				final List<String> v = new ArrayList<>();
				
				final int size = b.readVarInt();
				for (int i = 0; i < size; i++)
				{
					v.add(b.readString());
				}
				
				return () -> e.setSyncedValue(v);
			}
		));
	}
	
	public static void resetSyncedConfigs()
	{
		SYNCED_CONFIGS.values().forEach((entry) ->
		{
			entry.setSyncedValue(null);
		});
	}
	
	public static void syncConfigs(final Collection<ServerPlayerEntity> players)
	{
		for (final ServerPlayerEntity player : players)
		{
			syncConfigs(player.networkHandler, SYNCED_CONFIGS.values());
		}
	}
	
	public static void syncConfigs(final ServerPlayNetworkHandler networkHandler)
	{
		syncConfigs(networkHandler, SYNCED_CONFIGS.values());
	}
	
	public static void syncConfigs(final ServerPlayNetworkHandler networkHandler, final String... configEntryKeys)
	{
		final List<SyncableConfigEntry<?>> entries = new ArrayList<>();
		
		SyncableConfigEntry<?> entry;
		for (final String key : configEntryKeys)
		{
			if (SYNCED_CONFIGS.containsKey(key) && (entry = SYNCED_CONFIGS.get(key)) != null)
			{
				entries.add(entry);
			}
		}
		
		syncConfigs(networkHandler, entries);
	}
	
	private static final boolean NETWORKING_API_LOADED = ModLoaderUtils.isModLoaded("fabric-networking-api-v1");
	
	public static void syncConfigs(final ServerPlayNetworkHandler networkHandler, final Collection<SyncableConfigEntry<?>> configEntries)
	{
		if (NETWORKING_API_LOADED)
		{
			if (ServerPlayNetworking.canSend(networkHandler, Pehkui.CONFIG_SYNC_PACKET))
			{
				ReflectionUtils.sendPacket(networkHandler, createConfigSyncPacket(configEntries));
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Packet<?> createConfigSyncPacket(final Collection<SyncableConfigEntry<?>> configEntries)
	{
		final PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
		
		buffer.writeVarInt(configEntries.size());
		for (SyncableConfigEntry<?> entry : configEntries)
		{
			buffer.writeString(entry.getName());
			((ConfigEntryCodec) SYNCED_CONFIG_CODECS.get(entry.getName())).write(buffer, entry);
		}
		
		return ServerPlayNetworking.createS2CPacket(Pehkui.CONFIG_SYNC_PACKET, buffer);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Runnable readConfigs(final PacketByteBuf buffer)
	{
		final int qty = buffer.readVarInt();
		
		final List<Runnable> tasks = new ArrayList<>();
		
		String name;
		ConfigEntryCodec codec;
		SyncableConfigEntry entry;
		for (int i = 0; i < qty; i++)
		{
			name = buffer.readString();
			
			entry = SYNCED_CONFIGS.get(name);
			codec = SYNCED_CONFIG_CODECS.get(name);
			if (entry == null)
			{
				Pehkui.LOGGER.warn("Received unknown config \"{}\" from server.", name);
				break;
			}
			else if (codec == null)
			{
				Pehkui.LOGGER.warn("Codec \"{}\" not found. Could not parse config \"{}\" from server.", codec, name);
				break;
			}
			
			tasks.add(codec.read(buffer, entry));
		}
		
		return () -> tasks.forEach(Runnable::run);
	}
	
	private static class ConfigEntryCodec<T>
	{
		final BiConsumer<PacketByteBuf, SyncableConfigEntry<T>> writer;
		final BiFunction<PacketByteBuf, SyncableConfigEntry<T>, Runnable> reader;
		final Supplier<ArgumentType<T>> argumentGetter;
		final BiFunction<CommandContext<?>, String, T> argumentFunction;
		
		public ConfigEntryCodec(final BiConsumer<PacketByteBuf, SyncableConfigEntry<T>> writer, final BiFunction<PacketByteBuf, SyncableConfigEntry<T>, Runnable> reader)
		{
			this(writer, reader, () -> null, (c, n) -> null);
		}
		
		public ConfigEntryCodec(final BiConsumer<PacketByteBuf, SyncableConfigEntry<T>> writer, final BiFunction<PacketByteBuf, SyncableConfigEntry<T>, Runnable> reader, final Supplier<ArgumentType<T>> argumentGetter, final BiFunction<CommandContext<?>, String, T> argumentFunction)
		{
			this.writer = writer;
			this.reader = reader;
			this.argumentGetter = argumentGetter;
			this.argumentFunction = argumentFunction;
		}
		
		public void write(final PacketByteBuf buffer, final SyncableConfigEntry<T> entry)
		{
			writer.accept(buffer, entry);
		}
		
		public Runnable read(final PacketByteBuf buffer, final SyncableConfigEntry<T> entry)
		{
			return reader.apply(buffer, entry);
		}
		
		public @Nullable ArgumentType<T> getArgumentType()
		{
			return argumentGetter.get();
		}
		
		public T getArgument(final CommandContext<?> context, final String name)
		{
			return argumentFunction.apply(context, name);
		}
	}
	
	public static <T> MutableConfigEntry<T> createConfigEntry(final String name, final T defaultValue, final Supplier<T> supplier, final Consumer<T> consumer)
	{
		if (SYNCED_CONFIGS.containsKey(name))
		{
			@SuppressWarnings("unchecked")
			SyncableConfigEntry<T> entry = (SyncableConfigEntry<T>) SYNCED_CONFIGS.get(name);
			if (entry == null)
			{
				SYNCED_CONFIGS.put(name, entry = new SyncableConfigEntry<T>(name, defaultValue, supplier, consumer));
				CONFIGS.put(name, entry);
			}
			
			return entry;
		}
		
		final MutableConfigEntry<T> entry = new NamedConfigEntry<T>(name, defaultValue, supplier, consumer);
		
		CONFIGS.put(name, entry);
		
		return entry;
	}
	
	public static void setupSyncableConfig(final String name, final String codecKey)
	{
		SYNCED_CONFIGS.put(name, null);
		SYNCED_CONFIG_CODECS.put(name, Objects.requireNonNull(CODECS.get(codecKey), String.format("Codec \"%s\" not found for config \"%s\"", codecKey, name)));
	}
	
	public static ArgumentBuilder<ServerCommandSource, ?> registerConfigCommands()
	{
		final boolean splitConfigs = true;
		
		final ArgumentBuilder<ServerCommandSource, ?> builder = CommandManager.literal("config");
		
		ConfigSyncUtils.registerConfigSyncCommands(builder);
		ConfigSyncUtils.registerConfigFileCommands(builder);
		ConfigSyncUtils.registerConfigGetterCommands(builder, splitConfigs);
		ConfigSyncUtils.registerConfigSetterCommands(builder, splitConfigs);
		ConfigSyncUtils.registerConfigResetCommands(builder, splitConfigs);
		
		return builder;
	}
	
	public static void registerConfigSyncCommands(final ArgumentBuilder<ServerCommandSource, ?> configBuilder)
	{
		final ArgumentBuilder<ServerCommandSource, ?> builder = CommandManager.literal("sync")
			.executes(context ->
			{
				syncConfigs(context.getSource().getWorld().getServer().getPlayerManager().getPlayerList());
				
				return 1;
			});
		
		configBuilder.then(builder);
	}
	
	public static void registerConfigFileCommands(final ArgumentBuilder<ServerCommandSource, ?> configBuilder)
	{
		final JsonConfigHandler config = PehkuiConfig.BUILDER.config;
		
		configBuilder
			.then(CommandManager.literal("save")
				.executes(context ->
				{
					synchronized (config)
					{
						config.save();
					}
					
					return 1;
				})
			)
			.then(CommandManager.literal("load")
				.executes(context ->
				{
					synchronized (config)
					{
						config.invalidate();
						config.get();
						
						syncConfigs(context.getSource().getWorld().getServer().getPlayerManager().getPlayerList());
					}
					
					return 1;
				})
			)
			.then(CommandManager.literal("delete")
				.executes(context ->
				{
					synchronized (config)
					{
						config.onConfigChanged();
						try
						{
							Files.deleteIfExists(FabricLoader.getInstance().getConfigDir().resolve(Pehkui.MOD_ID).resolve("config.json").normalize());
							config.get();
							syncConfigs(context.getSource().getWorld().getServer().getPlayerManager().getPlayerList());
							
							return 1;
						}
						catch (IOException e)
						{
							Pehkui.LOGGER.catching(e);
							
							return 0;
						}
					}
				})
			);
	}
	
	public static void registerConfigGetterCommands(final ArgumentBuilder<ServerCommandSource, ?> configBuilder, final boolean splitKeys)
	{
		final ArgumentBuilder<ServerCommandSource, ?> builder = CommandManager.literal("get");
		
		String[] keys;
		ArgumentBuilder<ServerCommandSource, ?> root, temp;
		for (final String key : CONFIGS.keySet())
		{
			keys = splitKeys ? key.split("\\.") : new String[] { key };
			
			root = CommandManager.literal(keys[keys.length - 1])
				.executes(context ->
				{
					CommandUtils.sendFeedback(
						context.getSource(),
						() -> I18nUtils.translate(
							"commands.pehkui.debug.config.get.value",
							"Config \"%s\" is currently set to \"%s\"",
							key, String.valueOf(CONFIGS.get(key).getValue())
						),
						false
					);
					
					return 1;
				});
			
			for (int i = keys.length - 2; i >= 0; i--)
			{
				temp = CommandManager.literal(keys[i]);
				temp.then(root);
				root = temp;
			}
			
			builder.then(root);
		}
		
		configBuilder.then(builder);
	}
	
	public static void registerConfigSetterCommands(final ArgumentBuilder<ServerCommandSource, ?> configBuilder, final boolean splitKeys)
	{
		final ArgumentBuilder<ServerCommandSource, ?> builder = CommandManager.literal("set");
		
		registerConfigModificationCommands(builder, true, splitKeys);
		
		configBuilder.then(builder);
	}
	
	public static void registerConfigResetCommands(final ArgumentBuilder<ServerCommandSource, ?> configBuilder, final boolean splitKeys)
	{
		final ArgumentBuilder<ServerCommandSource, ?> builder = CommandManager.literal("reset");
		
		registerConfigModificationCommands(builder, false, splitKeys);
		
		builder.executes(context ->
		{
			for (final Entry<String, SyncableConfigEntry<?>> entry : SYNCED_CONFIGS.entrySet())
			{
				entry.getValue().reset();
			}
			
			CommandUtils.sendFeedback(
				context.getSource(),
				() -> I18nUtils.translate(
					"commands.pehkui.debug.config.reset",
					"%s config entries have been reset to their default values",
					String.valueOf(SYNCED_CONFIGS.size())
				),
				false
			);
			
			for (final ServerPlayerEntity p : context.getSource().getWorld().getServer().getPlayerManager().getPlayerList())
			{
				syncConfigs(p.networkHandler);
			}
			
			return 1;
		});
		
		configBuilder.then(builder);
	}
	
	private static void registerConfigModificationCommands(final ArgumentBuilder<ServerCommandSource, ?> builder, final boolean asSetterCommands, final boolean splitKeys)
	{
		ArgumentType<?> argType;
		String[] keys;
		ArgumentBuilder<ServerCommandSource, ?> root, temp;
		for (final Entry<String, SyncableConfigEntry<?>> entry : SYNCED_CONFIGS.entrySet())
		{
			final String key = entry.getKey();
			final ConfigEntryCodec<?> codec = SYNCED_CONFIG_CODECS.get(key);
			argType = codec.getArgumentType();
			
			if (argType == null)
			{
				continue;
			}
			
			keys = splitKeys ? key.split("\\.") : new String[] { key };
			
			root = (asSetterCommands ? CommandManager.argument("value", argType) : CommandManager.literal(keys[keys.length - 1]))
				.executes(context ->
				{
					final SyncableConfigEntry<?> cfg = entry.getValue();
					
					final String oldValue = String.valueOf(cfg.getValue());
					
					if (asSetterCommands)
					{
						setConfigValue(cfg, codec.getArgument(context, "value"));
					}
					else
					{
						cfg.reset();
					}
					
					final String newValue = String.valueOf(cfg.getValue());
					
					CommandUtils.sendFeedback(
						context.getSource(),
						() -> I18nUtils.translate(
							String.format("commands.pehkui.debug.config.%s.value", asSetterCommands ? "changed" : "reset"),
							asSetterCommands ? "Config \"%s\" was changed from \"%s\" to \"%s\"" : "Config \"%s\" was reset from \"%s\" to default \"%s\"",
							key, oldValue, newValue
						),
						false
					);
					
					final Collection<SyncableConfigEntry<?>> cfgs = Collections.singleton(cfg);
					
					for (final ServerPlayerEntity p : context.getSource().getWorld().getServer().getPlayerManager().getPlayerList())
					{
						syncConfigs(p.networkHandler, cfgs);
					}
					
					return 1;
				});
			
			for (int i = keys.length - (asSetterCommands ? 1 : 2); i >= 0; i--)
			{
				temp = CommandManager.literal(keys[i]);
				temp.then(root);
				root = temp;
			}
			
			builder.then(root);
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void setConfigValue(final MutableConfigEntry cfg, final Object value)
	{
		cfg.setValue(value);
	}
	
	private static class SyncableConfigEntry<T> extends NamedConfigEntry<T>
	{
		protected T syncedValue = null;
		
		public SyncableConfigEntry(final String name, final T defaultValue, final Supplier<T> supplier, final Consumer<T> consumer)
		{
			super(name, defaultValue, supplier, consumer);
		}
		
		public void setSyncedValue(final T value)
		{
			syncedValue = value;
		}
		
		public boolean isSynced()
		{
			return syncedValue != null;
		}
		
		@Override
		public T get()
		{
			return isSynced() ? syncedValue : super.get();
		}
		
		@Override
		public void accept(T t)
		{
			setSyncedValue(null);
			
			super.accept(t);
		}
		
		@Override
		public T getValue()
		{
			return isSynced() ? syncedValue : super.getValue();
		}
		
		@Override
		public void setValue(T t)
		{
			setSyncedValue(null);
			
			super.setValue(t);
		}
	}
	
	private static class NamedConfigEntry<T> implements MutableConfigEntry<T>
	{
		protected final String name;
		protected final Supplier<T> supplier;
		protected final Consumer<T> consumer;
		protected final T defaultValue;
		
		public NamedConfigEntry(final String name, final T defaultValue, final Supplier<T> supplier, final Consumer<T> consumer)
		{
			this.name = name;
			this.supplier = supplier;
			this.consumer = consumer;
			this.defaultValue = defaultValue;
		}
		
		public String getName()
		{
			return name;
		}
		
		public void reset()
		{
			setValue(defaultValue);
		}
		
		@Override
		public T get()
		{
			return supplier.get();
		}
		
		@Override
		public void accept(final T t)
		{
			consumer.accept(t);
		}
		
		@Override
		public T getValue()
		{
			return supplier.get();
		}
		
		@Override
		public void setValue(final T t)
		{
			consumer.accept(t);
		}
	}
}
