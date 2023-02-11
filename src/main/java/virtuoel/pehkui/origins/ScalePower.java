package virtuoel.pehkui.origins;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.LivingEntity;
import virtuoel.pehkui.Pehkui;

import java.util.Collections;
import java.util.List;

public class ScalePower extends Power
{

	private final List<ScaleTransformer> transformers;

	public ScalePower(PowerType<?> type, LivingEntity entity, List<ScaleTransformer> transformers)
	{
		super(type, entity);
		this.transformers = transformers;
	}

	@Override
	public void onGained()
	{
		for (ScaleTransformer transformer : transformers)
			transformer.applyTo(entity);
	}

	@Override
	public void onRespawn()
	{
		onGained();
	}

	@Override
	public void onLost()
	{
		List<ScalePower> scalePowers = PowerHolderComponent.getPowers(entity, ScalePower.class);
		for (ScaleTransformer transformer : transformers) {
			boolean shouldReset = scalePowers.stream().noneMatch(power -> power != this && power.transformers.stream().anyMatch(t -> t.getScaleType() == transformer.getScaleType())
			);
			if (shouldReset) // only reset if no other (new) powers are applying a transformer of the same scale type because onGained is called before onLost
				transformer.reset(entity);
		}
	}

	public static PowerFactory<ScalePower> createFactory()
	{
		return new PowerFactory<>(
				Pehkui.id("scale"),
				new SerializableData()
						.add("transformer", PehkuiDataTypes.SCALE_TRANSFORMER, null)
						.add("transformers", PehkuiDataTypes.SCALE_TRANSFORMERS, null),
				data -> (type, entity) ->
				{
					if (data.isPresent("transformer"))
						return new ScalePower(type, entity, Collections.singletonList(data.get("transformer")));
					if (data.isPresent("transformers"))
						return new ScalePower(type, entity, data.get("transformers"));
					return new ScalePower(type, entity, Collections.emptyList());
				}
		);
	}
}
