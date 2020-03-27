package virtuoel.pehkui.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class JsonConfigHandler extends ConfigHandler<JsonObject>
{
	public JsonConfigHandler(String namespace, String path, Supplier<JsonObject> defaultConfig)
	{
		super(namespace, path, defaultConfig);
	}
	
	@Override
	public JsonObject readConfig(Stream<String> lines)
	{
		return Streams.parse(new JsonReader(new StringReader(lines.collect(Collectors.joining("\n"))))).getAsJsonObject();
	}
	
	@Override
	public Iterable<? extends CharSequence> writeConfig(JsonObject configData)
	{
		try
		{
			final StringWriter stringWriter = new StringWriter();
			final JsonWriter jsonWriter = new JsonWriter(stringWriter);
			jsonWriter.setIndent("\t");
			Streams.write(configData, jsonWriter);
			stringWriter.write('\n');
			return Arrays.asList(stringWriter.toString().split("\n"));
		}
		catch (IOException e)
		{
			throw new AssertionError(e);
		}
	}
	
	@Override
	public JsonObject mergeConfigs(JsonObject readConfig, JsonObject defaultConfig)
	{
		return mergeJsonObjects(new JsonObject(), defaultConfig, readConfig);
	}
	
	private static JsonObject mergeJsonObjects(JsonObject dest, JsonObject... srcs)
	{
		for (final JsonObject src : srcs)
		{
			for (final Map.Entry<String, JsonElement> srcEntry : src.entrySet())
			{
				final String key = srcEntry.getKey();
				final JsonElement value = srcEntry.getValue();
				if (dest.has(key))
				{
					final JsonElement destValue = dest.get(key);
					if (destValue.isJsonArray() && value.isJsonArray())
					{
						final JsonArray leftArr = destValue.getAsJsonArray();
						for (final JsonElement element : value.getAsJsonArray())
						{
							leftArr.add(element);
						}
					}
					else if (destValue.isJsonObject() && value.isJsonObject())
					{
						mergeJsonObjects(destValue.getAsJsonObject(), value.getAsJsonObject());
					}
					else if (!value.isJsonNull())
					{
						dest.add(key, value);
					}
				}
				else
				{
					dest.add(key, value);
				}
			}
		}
		
		return dest;
	}
}
