package virtuoel.pehkui.origins;

import com.google.common.collect.BiMap;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import net.minecraft.util.Identifier;
import virtuoel.pehkui.api.*;

import java.util.List;

public class PehkuiDataTypes
{

	public static final SerializableDataType<ScaleType> SCALE_TYPE = registry(ScaleType.class, ScaleRegistries.SCALE_TYPES);
	public static final SerializableDataType<Float2FloatFunction> SCALE_EASING = registry(Float2FloatFunction.class, ScaleRegistries.SCALE_EASINGS);

	public static final SerializableDataType<ScaleOperation> SCALE_OPERATION = SerializableDataType.enumValue(ScaleOperation.class);
	public static final SerializableDataType<ScaleTransformer> SCALE_TRANSFORMER = SerializableDataType.compound(
			ScaleTransformer.class,
			new SerializableData()
					.add("scale", SCALE_TYPE, ScaleTypes.BASE)
					.add("value", SerializableDataTypes.FLOAT)
					.add("persist", SerializableDataTypes.BOOLEAN, true)
					.add("delay", SerializableDataTypes.INT, 20)
					.add("easing", SCALE_EASING, ScaleEasings.LINEAR),
			(dataInst) -> new ScaleTransformer(
					dataInst.get("scale"),
					dataInst.getFloat("value"),
					ScaleOperation.SET,
					dataInst.getBoolean("persist"),
					dataInst.getInt("delay"),
					dataInst.get("easing")),
			(data, inst) ->
			{
				SerializableData.Instance dataInst = data.new Instance();
				dataInst.set("scale", inst.getScaleType());
				dataInst.set("value", inst.getValue());
				dataInst.set("persist", inst.shouldPersist());
				dataInst.set("delay", inst.getDelay());
				dataInst.set("easing", inst.getEasing());
				return dataInst;
			});
	public static final SerializableDataType<List<ScaleTransformer>> SCALE_TRANSFORMERS = SerializableDataType.list(SCALE_TRANSFORMER);
	public static final SerializableDataType<ScaleTransformer> SCALE_TRANSFORMER_WITH_OPERATION = SerializableDataType.compound(
			ScaleTransformer.class,
			new SerializableData()
					.add("scale", SCALE_TYPE, ScaleTypes.BASE)
					.add("value", SerializableDataTypes.FLOAT)
					.add("persist", SerializableDataTypes.BOOLEAN, true)
					.add("operation", SCALE_OPERATION, ScaleOperation.SET)
					.add("delay", SerializableDataTypes.INT, 20)
					.add("easing", SCALE_EASING, ScaleEasings.LINEAR),
			(dataInst) -> new ScaleTransformer(
					dataInst.get("scale"),
					dataInst.getFloat("value"),
					dataInst.get("operation"),
					dataInst.getBoolean("persist"),
					dataInst.getInt("delay"),
					dataInst.get("easing")),
			(data, inst) ->
			{

				SerializableData.Instance dataInst = data.new Instance();
				dataInst.set("scale", inst.getScaleType());
				dataInst.set("value", inst.getValue());
				dataInst.set("operation", inst.getOperation());
				dataInst.set("persist", inst.shouldPersist());
				dataInst.set("delay", inst.getDelay());
				dataInst.set("easing", inst.getEasing());
				return dataInst;
			});
	public static final SerializableDataType<List<ScaleTransformer>> SCALE_TRANSFORMERS_WITH_OPERATION = SerializableDataType.list(SCALE_TRANSFORMER_WITH_OPERATION);

	private static <T> SerializableDataType<T> registry(Class<T> clazz, BiMap<Identifier, T> registry)
	{
		return SerializableDataType.wrap(clazz, SerializableDataTypes.IDENTIFIER, entry -> registry.inverse().get(entry), registry::get);
	}
}
