package virtuoel.pehkui.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class JsonConfigBuilder extends ConfigBuilder<JsonObject, JsonElement>
{
	public JsonConfigBuilder(final String namespace, final String path)
	{
		super(namespace, path);
	}
	
	@Override
	public <T extends Number> Supplier<T> numberConfig(final String member, Function<Number, T> mapper, T defaultValue)
	{
		defaultValues.add(c -> c.addProperty(member, defaultValue));
		
		final InvalidatableLazySupplier<T> entry = new InvalidatableLazySupplier<>(
			() -> Optional.ofNullable(config.get().get(member))
			.filter(JsonElement::isJsonPrimitive).map(JsonElement::getAsJsonPrimitive)
			.filter(JsonPrimitive::isNumber).map(JsonPrimitive::getAsNumber)
			.map(mapper).orElse(defaultValue)
		);
		
		config.addInvalidationListener(entry::invalidate);
		
		return entry;
	}
	
	@Override
	public Supplier<Boolean> booleanConfig(final String member, final boolean defaultValue)
	{
		defaultValues.add(c -> c.addProperty(member, defaultValue));
		
		final InvalidatableLazySupplier<Boolean> entry = new InvalidatableLazySupplier<>(
			() -> Optional.ofNullable(config.get().get(member))
			.filter(JsonElement::isJsonPrimitive).map(JsonElement::getAsJsonPrimitive)
			.filter(JsonPrimitive::isBoolean).map(JsonPrimitive::getAsBoolean)
			.orElse(defaultValue)
		);
		
		config.addInvalidationListener(entry::invalidate);
		
		return entry;
	}
	
	@Override
	public Supplier<List<String>> stringListConfig(final String config)
	{
		return listConfig(config, JsonElement::getAsString);
	}
	
	@Override
	public <T> Supplier<List<T>> listConfig(final String member, final Function<JsonElement, T> mapper)
	{
		defaultValues.add(c -> c.add(member, new JsonArray()));
		
		final InvalidatableLazySupplier<List<T>> entry = new InvalidatableLazySupplier<>(
			() -> Optional.ofNullable(config.get().get(member))
			.filter(JsonElement::isJsonArray).map(JsonElement::getAsJsonArray)
			.map(JsonArray::spliterator).map(a -> StreamSupport.stream(a, false))
			.map(s -> s.map(mapper).collect(Collectors.toList()))
			.orElseGet(ArrayList::new)
		);
		
		config.addInvalidationListener(entry::invalidate);
		
		return entry;
	}
	
	@Override
	protected ConfigHandler<JsonObject> createConfig()
	{
		return new JsonConfigHandler(
			namespace,
			path,
			() ->
			{
				final JsonObject defaultConfig = new JsonObject();
				
				for (final Consumer<JsonObject> value : defaultValues)
				{
					value.accept(defaultConfig);
				}
				
				return defaultConfig;
			}
		);
	}
}
