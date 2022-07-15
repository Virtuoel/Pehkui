package virtuoel.pehkui.api;

import net.minecraft.util.Identifier;
import virtuoel.pehkui.Pehkui;

public class ScaleEasings {

    public static final ScaleEasing LINEAR = register("linear", x -> x);

    private static ScaleEasing register(Identifier id, ScaleEasing easing)
    {
        return ScaleRegistries.register(
                ScaleRegistries.SCALE_EASINGS,
                id,
                easing
        );
    }

    private static ScaleEasing register(String path, ScaleEasing easing)
    {
        return register(Pehkui.id(path), easing);
    }

}
