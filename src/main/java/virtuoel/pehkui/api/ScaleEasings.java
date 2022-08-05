package virtuoel.pehkui.api;

import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import net.minecraft.util.Identifier;
import virtuoel.pehkui.Pehkui;

public final class ScaleEasings
{
	private static final double HALF_PI = Math.PI / 2.0D;
	
	public static final Float2FloatFunction LINEAR = register("linear", x -> x);
	
	public static final Float2FloatFunction QUADRATIC_IN = register("quadratic_in", x -> x * x);
	public static final Float2FloatFunction QUADRATIC_OUT = register("quadratic_out", x ->
	{
		return -(x * (x - 2.0F));
	});
	public static final Float2FloatFunction QUADRATIC_IN_OUT = register("quadratic_in_out", x ->
	{
		if (x < 0.5F)
		{
			return 2.0F * x * x;
		}
		else
		{
			return (-2.0F * x * x) + (4.0F * x) - 1.0F;
		}
	});
	
	public static final Float2FloatFunction CUBIC_IN = register("cubic_in", x -> x * x * x);
	public static final Float2FloatFunction CUBIC_OUT = register("cubic_out", x ->
	{
		float f = x - 1.0F;
		return f * f * f + 1.0F;
	});
	public static final Float2FloatFunction CUBIC_IN_OUT = register("cubic_in_out", x ->
	{
		if (x < 0.5F)
		{
			return 4.0F * x * x * x;
		}
		else
		{
			float f = (2.0F * x) - 2.0F;
			return 0.5F * f * f * f + 1.0F;
		}
	});
	
	public static final Float2FloatFunction QUARTIC_IN = register("quartic_in", x -> x * x * x * x);
	public static final Float2FloatFunction QUARTIC_OUT = register("quartic_out", x ->
	{
		float f = x - 1.0F;
		return f * f * f * (1.0F - x) + 1.0F;
	});
	public static final Float2FloatFunction QUARTIC_IN_OUT = register("quartic_in_out", x ->
	{
		if (x < 0.5F)
		{
			return 8.0F * x * x * x * x;
		}
		else
		{
			float f = x - 1.0F;
			return -8.0F * f * f * f * f + 1.0F;
		}
	});
	
	public static final Float2FloatFunction QUINTIC_IN = register("quintic_in", x -> x * x * x * x * x);
	public static final Float2FloatFunction QUINTIC_OUT = register("quintic_out", x ->
	{
		float f = x - 1.0F;
		return f * f * f * f * f + 1.0F;
	});
	public static final Float2FloatFunction QUINTIC_IN_OUT = register("quintic_in_out", x ->
	{
		if (x < 0.5F)
		{
			return 16.0F * x * x * x * x * x;
		}
		else
		{
			float f = (2.0F * x) - 2.0F;
			return 0.5F * f * f * f * f * f + 1.0F;
		}
	});
	
	public static final Float2FloatFunction SINE_IN = register("sine_in", x ->
	{
		return (float) Math.sin((x - 1.0F) * HALF_PI) + 1.0F;
	});
	public static final Float2FloatFunction SINE_OUT = register("sine_out", x ->
	{
		return (float) Math.sin(x * HALF_PI);
	});
	public static final Float2FloatFunction SINE_IN_OUT = register("sine_in_out", x ->
	{
		return (float) (0.5F * (1.0F - Math.cos(x * Math.PI)));
	});
	
	public static final Float2FloatFunction CIRCULAR_IN = register("circular_in", x ->
	{
		return (float) (1.0F - Math.sqrt(1.0F - (x * x)));
	});
	public static final Float2FloatFunction CIRCULAR_OUT = register("circular_out", x ->
	{
		return (float) Math.sqrt((2.0F - x) * x);
	});
	public static final Float2FloatFunction CIRCULAR_IN_OUT = register("circular_in_out", x ->
	{
		if (x < 0.5F)
		{
			return (float) (0.5F * (1.0F - Math.sqrt(1.0F - (4.0F * x * x))));
		}
		else
		{
			return (float) (0.5F * (Math.sqrt(1.0F - (4.0F * (1.0F - x) * (1.0F - x))) + 1.0F));
		}
	});
	
	public static final Float2FloatFunction EXPONENTIAL_IN = register("exponential_in", x ->
	{
		return (float) (x == 0.0F ? x : Math.pow(2.0F, 10.0F * (x - 1.0F)));
	});
	public static final Float2FloatFunction EXPONENTIAL_OUT = register("exponential_out", x ->
	{
		return (float) (x == 1.0F ? x : 1.0F - Math.pow(2.0F, -10.0F * x));
	});
	public static final Float2FloatFunction EXPONENTIAL_IN_OUT = register("exponential_in_out", x ->
	{
		if (x == 0.0F || x == 1.0F)
		{
			return x;
		}
		
		if (x < 0.5F)
		{
			return (float) (0.5F * Math.pow(2.0F, (20.0F * x) - 10.0F));
		}
		else
		{
			return (float) (-0.5F * Math.pow(2.0F, (-20.0F * x) + 10.0F) + 1.0F);
		}
	});
	
	public static final Float2FloatFunction ELASTIC_IN = register("elastic_in", x ->
	{
		return (float) (Math.sin(13.0F * HALF_PI * x) * Math.pow(2.0F, 10.0F * (x - 1.0F)));
	});
	public static final Float2FloatFunction ELASTIC_OUT = register("elastic_out", x ->
	{
		return (float) (Math.sin(-13.0F * HALF_PI * (x + 1.0F)) * Math.pow(2.0F, -10.0F * x) + 1.0F);
	});
	public static final Float2FloatFunction ELASTIC_IN_OUT = register("elastic_in_out", x ->
	{
		if (x < 0.5F)
		{
			return (float) (0.5F * Math.sin(13.0F * HALF_PI * (2.0F * x)) * Math.pow(2.0F, 10.0F * ((2.0F * x) - 1.0F)));
		}
		else
		{
			return (float) (0.5F * (Math.sin(-13.0F * HALF_PI * ((2.0F * x - 1.0F) + 1.0F)) * Math.pow(2.0F, -10.0F * (2.0F * x - 1.0F)) + 2.0F));
		}
	});
	
	public static final Float2FloatFunction BACK_IN = register("back_in", x ->
	{
		return (float) (x * x * x - x * Math.sin(x * Math.PI));
	});
	public static final Float2FloatFunction BACK_OUT = register("back_out", x ->
	{
		float f = 1.0F - x;
		return (float) (1.0F - (f * f * f - f * Math.sin(f * Math.PI)));
	});
	public static final Float2FloatFunction BACK_IN_OUT = register("back_in_out", x ->
	{
		if (x < 0.5F)
		{
			float f = 2.0F * x;
			return (float) (0.5F * (f * f * f - f * Math.sin(f * Math.PI)));
		}
		else
		{
			float f = 1.0F - (2.0F * x - 1.0F);
			return (float) (0.5F * (1.0F - (f * f * f - f * Math.sin(f * Math.PI))) + 0.5F);
		}
	});
	
	public static final Float2FloatFunction BOUNCE_IN = register("bounce_in", x ->
	{
		return 1.0F - ScaleEasings.BOUNCE_OUT.apply(1.0F - x);
	});
	public static final Float2FloatFunction BOUNCE_OUT = register("bounce_out", x ->
	{
		if (x < 4.0F / 11.0F)
		{
			return (121.0F * x * x) / 16.0F;
		}
		else if (x < 8.0F / 11.0F)
		{
			return (363.0F / 40.0F * x * x) - (99.0F / 10.0F * x) + 17.0F / 5.0F;
		}
		else if (x < 9 / 10.0)
		{
			return (4356.0F / 361.0F * x * x) - (35442.0F / 1805.0F * x) + 16061.0F / 1805.0F;
		}
		else
		{
			return (54.0F / 5.0F * x * x) - (513.0F / 25.0F * x) + 268.0F / 25.0F;
		}
	});
	public static final Float2FloatFunction BOUNCE_IN_OUT = register("bounce_in_out", x ->
	{
		if (x < 0.5F)
		{
			return 0.5F * ScaleEasings.BOUNCE_IN.apply(x * 2.0F);
		}
		else
		{
			return 0.5F * ScaleEasings.BOUNCE_OUT.apply(x * 2.0F - 1.0F) + 0.5F;
		}
	});
	
	private static Float2FloatFunction register(String path, Float2FloatFunction easing)
	{
		return register(Pehkui.id(path), easing);
	}
	
	private static Float2FloatFunction register(Identifier id, Float2FloatFunction easing)
	{
		return ScaleRegistries.register(ScaleRegistries.SCALE_EASINGS, id, easing);
	}
	
	private ScaleEasings()
	{
		
	}
}
