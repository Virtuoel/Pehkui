package virtuoel.pehkui.origins;

import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import virtuoel.pehkui.Pehkui;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class PehkuiPowers
{
	public static void register()
	{
		registerPower(ScalePower.createFactory());
		registerEntityAction("scale", ScaleEntityAction.DATA, new ScaleEntityAction());
		registerEntityCondition("scale", ScaleEntityCondition.DATA, new ScaleEntityCondition());
	}

	private static void registerPower(PowerFactory<?> powerFactory)
	{
		Registry.register(ApoliRegistries.POWER_FACTORY, powerFactory.getSerializerId(), powerFactory);
	}

	private static void registerEntityAction(String path, SerializableData data, BiConsumer<SerializableData.Instance, Entity> effect)
	{
		Identifier id = Pehkui.id(path);
		Registry.register(ApoliRegistries.ENTITY_ACTION, id, new ActionFactory<>(id, data, effect));
	}

	private static void registerEntityCondition(String path, SerializableData data, BiFunction<SerializableData.Instance, Entity, Boolean> effect)
	{
		Identifier id = Pehkui.id(path);
		Registry.register(ApoliRegistries.ENTITY_CONDITION, id, new ConditionFactory<>(id, data, effect));
	}
}
