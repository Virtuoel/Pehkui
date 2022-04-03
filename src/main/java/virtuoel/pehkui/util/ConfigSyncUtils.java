package virtuoel.pehkui.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import virtuoel.kanos_config.api.MutableConfigEntry;
import virtuoel.pehkui.Pehkui;

public class ConfigSyncUtils
{
	private static final Map<String, SyncableConfigEntry<?>> SYNCED_CONFIGS = new HashMap<>();
	private static final Map<String, ConfigEntryCodec<?>> SYNCED_CONFIG_CODECS = new HashMap<>();
	private static final Map<String, ConfigEntryCodec<?>> CODECS = new HashMap<>();
	
	static
	{
		CODECS.put("double", new ConfigEntryCodec<Double>(
			(b, e) ->
			{
				final double v = b.readDouble();
				
				return () -> e.setSyncedValue(v);
			},
			(b, e) -> b.writeDouble(e.getValue())
		));
		CODECS.put("boolean", new ConfigEntryCodec<Boolean>(
			(b, e) ->
			{
				final boolean v = b.readBoolean();
				
				return () -> e.setSyncedValue(v);
			},
			(b, e) -> b.writeBoolean(e.getValue())
		));
		CODECS.put("string_list", new ConfigEntryCodec<List<String>>(
			(b, e) ->
			{
				final List<String> v = new ArrayList<>();
				
				final int size = b.readVarInt();
				for (int i = 0; i < size; i++)
				{
					v.add(b.readString());
				}
				
				return () -> e.setSyncedValue(v);
			},
			(b, e) ->
			{
				final List<String> list = e.getValue();
				
				b.writeVarInt(list.size());
				for (final String v : list)
				{
					b.writeString(v);
				}
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
	
	public static void writeConfigs(final ServerPlayNetworkHandler networkHandler)
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void syncConfigs(final ServerPlayNetworkHandler networkHandler, final Collection<SyncableConfigEntry<?>> configEntries)
	{
		if (ServerPlayNetworking.canSend(networkHandler, Pehkui.CONFIG_SYNC_PACKET))
		{
			final PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
			
			buffer.writeVarInt(configEntries.size());
			for (SyncableConfigEntry<?> entry : configEntries)
			{
				buffer.writeString(entry.getName());
				((ConfigEntryCodec) SYNCED_CONFIG_CODECS.get(entry.getName())).write(buffer, entry);
			}
			
			networkHandler.sendPacket(new CustomPayloadS2CPacket(Pehkui.CONFIG_SYNC_PACKET, buffer));
		}
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
			
			codec = SYNCED_CONFIG_CODECS.get(name);
			entry = SYNCED_CONFIGS.get(name);
			if (codec == null || entry == null)
			{
				break;
			}
			
			tasks.add(codec.read(buffer, entry));
		}
		
		return () -> tasks.forEach(Runnable::run);
	}
	
	private static class ConfigEntryCodec<T>
	{
		final BiFunction<PacketByteBuf, SyncableConfigEntry<T>, Runnable> reader;
		final BiConsumer<PacketByteBuf, SyncableConfigEntry<T>> writer;
		
		public ConfigEntryCodec(final BiFunction<PacketByteBuf, SyncableConfigEntry<T>, Runnable> reader, final BiConsumer<PacketByteBuf, SyncableConfigEntry<T>> writer)
		{
			this.reader = reader;
			this.writer = writer;
		}
		
		public Runnable read(final PacketByteBuf buffer, final SyncableConfigEntry<T> entry)
		{
			return reader.apply(buffer, entry);
		}
		
		public void write(final PacketByteBuf buffer, final SyncableConfigEntry<T> entry)
		{
			writer.accept(buffer, entry);
		}
	}
	
	public static <T> MutableConfigEntry<T> createSyncedConfig(final String name, final Supplier<T> supplier, final Consumer<T> consumer)
	{
		if (SYNCED_CONFIGS.containsKey(name) && SYNCED_CONFIGS.get(name) == null)
		{
			final SyncableConfigEntry<T> entry = new SyncableConfigEntry<T>(name, supplier, consumer);
			
			SYNCED_CONFIGS.put(name, entry);
			
			return entry;
		}
		
		return new NamedConfigEntry<T>(name, supplier, consumer);
	}
	
	public static String markConfigForSync(final String name, final String codecKey)
	{
		SYNCED_CONFIGS.put(name, null);
		SYNCED_CONFIG_CODECS.put(name, Objects.requireNonNull(CODECS.get(codecKey), String.format("Codec \"%s\" not found for config \"%s\"", codecKey, name)));
		return name;
	}
	
	private static class SyncableConfigEntry<T> extends NamedConfigEntry<T>
	{
		protected T syncedValue = null;
		
		public SyncableConfigEntry(final String name, final Supplier<T> supplier, final Consumer<T> consumer)
		{
			super(name, supplier, consumer);
		}
		
		public void setSyncedValue(final T value)
		{
			syncedValue = value;
		}
		
		@Override
		public T get()
		{
			if (syncedValue != null)
			{
				return syncedValue;
			}
			
			return super.get();
		}
		
		@Override
		public T getValue()
		{
			if (syncedValue != null)
			{
				return syncedValue;
			}
			
			return super.getValue();
		}
	}
	
	private static class NamedConfigEntry<T> implements MutableConfigEntry<T>
	{
		protected final String name;
		protected final Supplier<T> supplier;
		protected final Consumer<T> consumer;
		
		public NamedConfigEntry(final String name, final Supplier<T> supplier, final Consumer<T> consumer)
		{
			this.name = name;
			this.supplier = supplier;
			this.consumer = consumer;
		}
		
		public String getName()
		{
			return name;
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
