package virtuoel.pehkui.util;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;
import java.util.function.Supplier;

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
		super(namespace, path, defaultConfig,
			JsonConfigHandler::readConfig,
			JsonConfigHandler::writeConfig,
			JsonConfigHandler::mergeConfigs
		);
	}
	
	public static JsonObject readConfig(Reader reader)
	{
		return Streams.parse(new JsonReader(reader)).getAsJsonObject();
	}
	
	public static void writeConfig(Writer writer, JsonObject configData)
	{
		try
		{
			final JsonWriter jsonWriter = new JsonWriter(writer);
			jsonWriter.setIndent("\t");
			Streams.write(configData, jsonWriter);
			writer.write('\n');
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static JsonObject mergeConfigs(JsonObject readConfig, JsonObject defaultConfig)
	{
		return mergeJsonObjects(new JsonObject(), defaultConfig, readConfig);
	}
	
	public static JsonObject mergeJsonObjects(JsonObject dest, JsonObject... srcs)
	{
		for(JsonObject src : srcs)
		{
			for(Map.Entry<String, JsonElement> srcEntry : src.entrySet())
			{
				final String key = srcEntry.getKey();
				final JsonElement value = srcEntry.getValue();
				if(dest.has(key))
				{
					final JsonElement destValue = dest.get(key);
					if(destValue.isJsonArray() && value.isJsonArray())
					{
						JsonArray leftArr = destValue.getAsJsonArray();
						for(JsonElement element : value.getAsJsonArray())
						{
							leftArr.add(element);
						}
					}
					else if(destValue.isJsonObject() && value.isJsonObject())
					{
						mergeJsonObjects(destValue.getAsJsonObject(), value.getAsJsonObject());
					}
					else if(!value.isJsonNull())
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
