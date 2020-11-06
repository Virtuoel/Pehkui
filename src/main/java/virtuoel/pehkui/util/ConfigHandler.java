package virtuoel.pehkui.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.loader.api.FabricLoader;

public abstract class ConfigHandler<S> implements Supplier<S>
{
	private final String namespace;
	protected final Logger logger;
	private final Path configFile;
	protected final Supplier<S> defaultConfig;
	private S cachedConfig = null;
	
	public ConfigHandler(String namespace, String path, Supplier<S> defaultConfig)
	{
		this.namespace = namespace;
		logger = LogManager.getLogger(namespace);
		configFile = FabricLoader.getInstance().getConfigDir().resolve(path);
		this.defaultConfig = defaultConfig;
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
		S configData = null;
		try
		{
			Files.createDirectories(configFile.getParent());
			if (Files.exists(configFile))
			{
				configData = readConfig(Files.lines(configFile));
			}
		}
		catch (IOException e)
		{
			logger.catching(e);
		}
		
		final S defaultData = defaultConfig.get();
		if (!Objects.equals(configData, defaultData))
		{
			final S mergedData = configData == null ? defaultData : mergeConfigs(configData, defaultData);
			if (!Objects.equals(configData, mergedData))
			{
				configData = mergedData;
				save(configData);
			}
		}
		
		return configData;
	}
	
	public void save()
	{
		save(get());
	}
	
	public void save(S configData)
	{
		try
		{
			Files.write(configFile, writeConfig(configData));
		}
		catch (IOException e)
		{
			logger.warn("Failed to write config.");
			logger.catching(e);
		}
	}
	
	protected abstract S readConfig(Stream<String> lines);
	
	protected abstract Iterable<? extends CharSequence> writeConfig(S configData);
	
	protected abstract S mergeConfigs(S configData, S defaultData);
}
