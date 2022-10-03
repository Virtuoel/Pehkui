package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.command.DataCommandObject;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.server.command.DataCommand;
import net.minecraft.server.command.ServerCommandSource;

@Mixin(DataCommand.class)
public interface DataCommandInvoker
{
	@Invoker
	public static int callExecuteGet(ServerCommandSource source, DataCommandObject object)
	{
		throw new NoSuchMethodError();
	}
	
	@Invoker
	public static int callExecuteGet(ServerCommandSource source, DataCommandObject object, NbtPathArgumentType.NbtPath path)
	{
		throw new NoSuchMethodError();
	}
	
	@Invoker
	public static int callExecuteGet(ServerCommandSource source, DataCommandObject object, NbtPathArgumentType.NbtPath path, double scale)
	{
		throw new NoSuchMethodError();
	}
}
