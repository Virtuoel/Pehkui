package virtuoel.pehkui.mixin.compat119plus;

import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.brigadier.arguments.ArgumentType;

import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.serialize.ArgumentSerializer;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.util.registry.Registry;
import virtuoel.pehkui.util.CommandUtils;
import virtuoel.pehkui.util.CommandUtils.ArgumentTypeConsumer;

@Mixin(ArgumentTypes.class)
public class ArgumentTypesMixin
{
	/*
	@Shadow
	private static <A extends ArgumentType<?>, T extends ArgumentSerializer.ArgumentTypeProperties<A>> ArgumentSerializer<A, T> register(Registry<ArgumentSerializer<?, ?>> registry, String string, Class<? extends A> class_, ArgumentSerializer<A, T> argumentSerializer)
	{
		return argumentSerializer;
	}
	
	@Inject(at = @At("RETURN"), method = "register(Lnet/minecraft/util/registry/Registry;)Lnet/minecraft/command/argument/serialize/ArgumentSerializer;")
	private static void onRegister(Registry<ArgumentSerializer<?, ?>> registry, CallbackInfoReturnable<ArgumentSerializer<?, ?>> info)
	{
		CommandUtils.registerArgumentTypes(new ArgumentTypeConsumer()
		{
			@Override
			public <T extends ArgumentType<?>> void register(String id, Class<T> argClass, Supplier<T> supplier)
			{
				ArgumentTypesMixin.register(registry, id.toString(), argClass, ConstantArgumentSerializer.of(supplier));
			}
		});
	}
	*/
}
