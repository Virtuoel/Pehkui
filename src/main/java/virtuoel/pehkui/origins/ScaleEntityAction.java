package virtuoel.pehkui.origins;

import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;

import java.util.List;
import java.util.function.BiConsumer;

public class ScaleEntityAction implements BiConsumer<SerializableData.Instance, Entity>
{
	public static final SerializableData DATA = new SerializableData()
			.add("transformer", PehkuiDataTypes.SCALE_TRANSFORMER_WITH_OPERATION, null)
			.add("transformers", PehkuiDataTypes.SCALE_TRANSFORMERS_WITH_OPERATION, null);

	@Override
	public void accept(SerializableData.Instance data, Entity entity)
	{
		if (data.isPresent("transformer"))
			data.<ScaleTransformer>get("transformer").applyTo(entity);
		if (data.isPresent("transformers"))
			data.<List<ScaleTransformer>>get("transformers").forEach(transformer -> transformer.applyTo(entity));
	}
}
