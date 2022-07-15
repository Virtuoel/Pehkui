package virtuoel.pehkui.api;

import net.minecraft.util.Identifier;

@FunctionalInterface
public interface ScaleEasing
{

    float ease(float x);

    default Identifier getId() {
        return ScaleRegistries.getId(ScaleRegistries.SCALE_EASINGS, this);
    }

}
