package virtuoel.pehkui.api;

import java.util.function.DoubleBinaryOperator;

import net.minecraft.util.Identifier;
import virtuoel.pehkui.Pehkui;

public class ScaleOperations
{
	public static final DoubleBinaryOperator NOOP = register("noop", (curr, arg) -> curr);
	public static final DoubleBinaryOperator SET = register("set", (curr, arg) -> arg);
	public static final DoubleBinaryOperator ADD = register("add", (curr, arg) -> curr + arg);
	public static final DoubleBinaryOperator SUBTRACT = register("subtract", (curr, arg) -> curr - arg);
	public static final DoubleBinaryOperator MULTIPLY = register("multiply", (curr, arg) -> curr * arg);
	public static final DoubleBinaryOperator DIVIDE = register("divide", (curr, arg) -> curr / arg);
	public static final DoubleBinaryOperator POWER = register("power", (curr, arg) -> (float) Math.pow(curr, arg));
	
	private static DoubleBinaryOperator register(String path, DoubleBinaryOperator easing)
	{
		return register(Pehkui.id(path), easing);
	}
	
	private static DoubleBinaryOperator register(Identifier id, DoubleBinaryOperator easing)
	{
		return ScaleRegistries.register(ScaleRegistries.SCALE_OPERATIONS, id, easing);
	}
	
	private ScaleOperations()
	{
		
	}
}
