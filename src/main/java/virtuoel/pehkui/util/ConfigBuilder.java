package virtuoel.pehkui.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class ConfigBuilder<R, E>
{
	protected final String namespace, path;
	protected final Collection<Consumer<R>> defaultValues;
	public final ConfigHandler<R> config;
	
	public ConfigBuilder(final String namespace, final String path)
	{
		this.namespace = namespace;
		this.path = path;
		this.defaultValues = new ArrayList<>();
		this.config = createConfig();
	}
	
	public Supplier<Double> doubleConfig(final String config, final double defaultValue)
	{
		return numberConfig(config, Number::doubleValue, defaultValue);
	}
	
	public abstract <T extends Number> Supplier<T> numberConfig(final String member, Function<Number, T> mapper, T defaultValue);
	
	public abstract Supplier<Boolean> booleanConfig(final String member, final boolean defaultValue);
	
	public abstract Supplier<List<String>> stringListConfig(final String config);
	
	public abstract <T> Supplier<List<T>> listConfig(final String member, final Function<E, T> mapper);
	
	protected abstract ConfigHandler<R> createConfig();
}
