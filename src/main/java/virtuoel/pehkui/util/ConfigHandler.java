package virtuoel.pehkui.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.loader.api.FabricLoader;

public class ConfigHandler<S> implements Supplier<S>
{
	private final String namespace;
	protected final Logger logger;
	private final File configFile;
	protected final Supplier<S> defaultConfig;
	private final Function<Reader, S> configReader;
	private final BiConsumer<Writer, S> configWriter;
	private final BiFunction<S, S, S> configMerger;
	private S cachedConfig = null;
	
	public ConfigHandler(String namespace, String path, Supplier<S> defaultConfig, Function<Reader, S> configReader, BiConsumer<Writer, S> configWriter, BiFunction<S, S, S> configMerger)
	{
		this.namespace = namespace;
		logger = LogManager.getLogger(namespace);
		configFile = new File(FabricLoader.getInstance().getConfigDirectory(), path);
		this.defaultConfig = defaultConfig;
		this.configReader = configReader;
		this.configWriter = configWriter;
		this.configMerger = configMerger;
	}
	
	public String getNamespace()
	{
		return namespace;
	}
	
	@Override
	public S get()
	{
		return cachedConfig != null ? cachedConfig : (cachedConfig = load());
	}
	
	public S load()
	{
		return load(logger, configFile, defaultConfig, configReader, configWriter, configMerger);
	}
	
	public static <T> T load(Logger logger, File configFile, Supplier<T> defaultConfig, Function<Reader, T> configReader, BiConsumer<Writer, T> configWriter, BiFunction<T, T, T> configMerger)
	{
		T configData = null;
		configFile.getParentFile().mkdirs();
		if(configFile.exists())
		{
			try(final FileReader reader = new FileReader(configFile))
			{
				configData = configReader.apply(reader);
			}
			catch(IOException e)
			{
				logger.catching(e);
			}
		}
		
		final T defaultData = defaultConfig.get();
		if(!Objects.equals(configData, defaultData))
		{
			final T mergedData = configData == null ? defaultData : configMerger.apply(configData, defaultData);
			if(!Objects.equals(configData, mergedData))
			{
				configData = mergedData;
				save(logger, configData, configFile, configWriter);
			}
		}
		
		return configData;
	}
	
	public void save()
	{
		save(logger, get(), configFile, configWriter);
	}
	
	public static <T> void save(Logger logger, T configData, File configFile, BiConsumer<Writer, T> configWriter)
	{
		try(final FileWriter writer = new FileWriter(configFile))
		{
			configWriter.accept(writer, configData);
		}
		catch(IOException e)
		{
			logger.warn("Failed to write config.");
			logger.catching(e);
		}
	}
}
