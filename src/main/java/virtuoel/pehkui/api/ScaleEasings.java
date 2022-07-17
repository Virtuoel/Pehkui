package virtuoel.pehkui.api;

import net.minecraft.util.Identifier;
import virtuoel.pehkui.Pehkui;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

public class ScaleEasings {

    private static final float c1 = 1.70158F;
    private static final float c4 = (float) ((2 * Math.PI) / 3);
    private static final float c5 = (float) ((2 * Math.PI) / 4.5);
    private static final float n1 = 7.5625F;
    private static final float d1 = 2.75F;

    public static final ScaleEasing LINEAR = register("linear", x -> x);
    public static final ScaleEasing SINE_IN = register("sine_in", x -> (float) (1 - cos((x * PI) / 2)));
    public static final ScaleEasing SINE_OUT = register("sine_out", x -> (float) sin((x * PI) / 2));
    public static final ScaleEasing SINE_IN_OUT = register("sine_in_out", x -> (float) -(cos(PI * x) - 1) / 2);
    public static final ScaleEasing QUAD_IN = register("quad_in", x -> x * x);
    public static final ScaleEasing QUAD_OUT = register("quad_out", x -> 1 - (1 - x) * (1 - x));
    public static final ScaleEasing QUAD_IN_OUT = register("quad_in_out", x -> x < 0.5 ? 2 * x * x : (float) (1 - pow(-2 * x + 2, 2) / 2));
    public static final ScaleEasing CUBIC_IN = register("cubic_in", x -> x * x * x);
    public static final ScaleEasing CUBIC_OUT = register("cubic_out", x -> (float) (1 - pow(1 - x, 3)));
    public static final ScaleEasing CUBIC_IN_OUT = register("cubic_in_out", x -> x < 0.5 ? 4 * x * x * x : (float) (1 - pow(-2 * x + 2, 3) / 2));
    public static final ScaleEasing QUART_IN = register("quart_in", x -> x * x * x * x);
    public static final ScaleEasing QUART_OUT = register("quart_out", x -> (float) (1 - pow(1 - x, 4)));
    public static final ScaleEasing QUART_IN_OUT = register("quart_in_out", x -> x < 0.5 ? 8 * x * x * x * x : (float) (1 - pow(-2 * x + 2, 4) / 2));
    public static final ScaleEasing QUINT_IN = register("quint_in", x -> x * x * x * x * x);
    public static final ScaleEasing QUINT_OUT = register("quint_out", x -> (float) (1 - pow(1 - x, 5)));
    public static final ScaleEasing QUINT_IN_OUT = register("quint_in_out", x -> x < 0.5 ? 16 * x * x * x * x * x : (float) (1 - pow(-2 * x + 2, 5) / 2));
    public static final ScaleEasing EXPO_IN = register("expo_in", x -> x == 0 ? 0 : (float) pow(2, 10 * x - 10));
    public static final ScaleEasing EXPO_OUT = register("expo_out", x -> x == 1 ? 1 : (float) (1 - pow(2, -10 * x)));
    public static final ScaleEasing EXPO_IN_OUT = register("expo_in_out", x -> x == 0 ? 0 : (float) (x == 1 ? 1 : x < 0.5 ? pow(2, 20 * x - 10) / 2 : (2 - pow(2, -20 * x + 10)) / 2));
    public static final ScaleEasing CIRC_IN = register("circ_in", x -> (float) (1 - sqrt(1 - pow(x, 2))));
    public static final ScaleEasing CIRC_OUT = register("circ_out", x -> (float) sqrt(1 - pow(x - 1, 2)));
    public static final ScaleEasing CIRC_IN_OUT = register("circ_in_out", x -> (float) (x < 0.5 ? (1 - sqrt(1 - pow(2 * x, 2))) / 2 : (sqrt(1 - pow(-2 * x + 2, 2)) + 1) / 2));
    public static final ScaleEasing BACK_IN = register("back_in", x -> {
        final float c3 = c1 + 1;
        return c3 * x * x * x - c1 * x * x;
    });
    public static final ScaleEasing BACK_OUT = register("back_out", x -> {
        final float c3 = c1 + 1;
        return (float) (1 + c3 * pow(x - 1, 3) + c1 * pow(x - 1, 2));
    });
    public static final ScaleEasing BACK_IN_OUT = register("back_in_out", x -> {
        final float c2 = c1 * 1.525F;
        return (float) (x < 0.5 ? (pow(2 * x, 2) * ((c2 + 1) * 2 * x - c2)) / 2 : (pow(2 * x - 2, 2) * ((c2 + 1) * (x * 2 - 2) + c2) + 2) / 2);
    });
    public static final ScaleEasing ELASTIC_IN = register("elastic_in", x -> x == 0 ? 0 : (float) (x == 1 ? 1 : -pow(2, 10 * x - 10) * sin((x * 10 - 10.75) * c4)));
    public static final ScaleEasing ELASTIC_OUT = register("elastic_out", x -> x == 0 ? 0 : (float) (x == 1 ? 1 : pow(2, -10 * x) * sin((x * 10 - 0.75) * c4) + 1));
    public static final ScaleEasing ELASTIC_IN_OUT = register("elastic_in_out", x -> x == 0 ? 0 : (float) (x == 1 ? 1 : x < 0.5 ? -(pow(2, 20 * x - 10) * sin((20 * x - 11.125) * c5)) / 2 : (pow(2, -20 * x + 10) * sin((20 * x - 11.125) * c5)) / 2 + 1));
    public static final ScaleEasing BOUNCE_OUT = register("bounce_out", x -> {
        if (x < 1 / d1)
            return n1 * x * x;
        else if (x < 2 / d1)
            return n1 * (x -= 1.5 / d1) * x + 0.75F;
        else if (x < 2.5 / d1)
            return n1 * (x -= 2.25 / d1) * x + 0.9375F;
        else
            return n1 * (x -= 2.625 / d1) * x + 0.984375F;
    });
    public static final ScaleEasing BOUNCE_IN = register("bounce_in", x -> 1 - BOUNCE_OUT.ease(1 - x));
    public static final ScaleEasing BOUNCE_IN_OUT = register("bounce_in_out", x -> x < 0.5 ? (1 - BOUNCE_OUT.ease(1 - 2 * x)) / 2 : (1 + BOUNCE_OUT.ease(2 * x - 1)) / 2);

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
