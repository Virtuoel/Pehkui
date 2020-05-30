package virtuoel.pehkui.mixin.client.compat116plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.client.MinecraftClient;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin
{
	@ModifyArg(method = "method_29337", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_5350;method_29466(Ljava/util/List;ZILjava/util/concurrent/Executor;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"))
	private boolean setIntegrated(boolean in)
	{
		return false;
	}
}
