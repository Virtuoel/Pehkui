package virtuoel.pehkui.origins;

import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.registry.Registry;

public class PehkuiPowers
{
	public static void register()
	{
		registerPower(ScalePower.createFactory());
	}

	private static void registerPower(PowerFactory<?> powerFactory)
	{
		Registry.register(ApoliRegistries.POWER_FACTORY, powerFactory.getSerializerId(), powerFactory);
	}
}
