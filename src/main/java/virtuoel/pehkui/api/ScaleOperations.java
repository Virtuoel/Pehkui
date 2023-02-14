package virtuoel.pehkui.api;

import it.unimi.dsi.fastutil.floats.FloatBinaryOperator;
import net.minecraft.util.Identifier;
import virtuoel.pehkui.Pehkui;

public class ScaleOperations
{
	public static final FloatBinaryOperator NOOP = register("noop", (curr, arg) -> curr);
	public static final FloatBinaryOperator SET = register("set", (curr, arg) -> arg);
	public static final FloatBinaryOperator ADD = register("add", (curr, arg) -> curr + arg);
	public static final FloatBinaryOperator SUBTRACT = register("subtract", (curr, arg) -> curr - arg);
	public static final FloatBinaryOperator MULTIPLY = register("multiply", (curr, arg) -> curr * arg);
	public static final FloatBinaryOperator DIVIDE = register("divide", (curr, arg) -> curr / arg);
	public static final FloatBinaryOperator POWER = register("power", (curr, arg) -> (float) Math.pow(curr, arg));
	
	private static FloatBinaryOperator register(String path, FloatBinaryOperator easing)
	{
		return register(Pehkui.id(path), easing);
	}
	
	private static FloatBinaryOperator register(Identifier id, FloatBinaryOperator easing)
	{
		return ScaleRegistries.register(ScaleRegistries.SCALE_OPERATIONS, id, easing);
	}
	
	private ScaleOperations()
	{
		
	}
}
