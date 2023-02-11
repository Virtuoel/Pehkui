package virtuoel.pehkui.origins;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.CreeperEntity;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;

import java.util.function.BiFunction;

public class ScaleEntityCondition implements BiFunction<SerializableData.Instance, Entity, Boolean>
{
	public static final SerializableData DATA = new SerializableData()
			.add("scale", PehkuiDataTypes.SCALE_TYPE)
			.add("use_target_value", SerializableDataTypes.BOOLEAN, false)
			.add("comparison", ApoliDataTypes.COMPARISON)
			.add("compare_to", SerializableDataTypes.FLOAT);

	@Override
	public Boolean apply(SerializableData.Instance data, Entity entity)
	{
		ScaleType scaleType = data.get("scale");
		Comparison comparison = data.get("comparison");
		float compareTo = data.getFloat("compare_to");

		ScaleData scaleData = scaleType.getScaleData(entity);
		float value = data.getBoolean("use_target_value") ? scaleData.getTargetScale() : scaleData.getScale();
		return comparison.compare(value, compareTo);
	}
}
