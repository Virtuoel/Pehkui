package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.minecraft.command.argument.OperationArgumentType;

@Mixin(OperationArgumentType.class)
public interface OperationArgumentTypeAccessor
{
	@Accessor("INVALID_OPERATION")
	public static SimpleCommandExceptionType getInvalidOperationException()
	{
		throw new UnsupportedOperationException();
	}
	
	@Accessor("DIVISION_ZERO_EXCEPTION")
	public static SimpleCommandExceptionType getDivisionZeroException()
	{
		throw new UnsupportedOperationException();
	}
}
