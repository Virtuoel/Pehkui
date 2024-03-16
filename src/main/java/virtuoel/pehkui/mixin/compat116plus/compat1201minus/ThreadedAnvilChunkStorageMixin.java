package virtuoel.pehkui.mixin.compat116plus.compat1201minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.world.chunk.WorldChunk;

@Mixin(ThreadedAnvilChunkStorage.class)
public abstract class ThreadedAnvilChunkStorageMixin
{
	@WrapOperation(method = { "method_17227", "func_219237_a_", "m_180935_", "m_202988_", "m_214854_" }, require = 0, expect = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/WorldChunk;loadToWorld()V"))
	private void pehkui$convertToFullChunk$lambda$loadEntities(WorldChunk obj, Operation<Void> original, @Local(argsOnly = true, index = 0) ChunkHolder arg)
	{
		final ChunkHolderAccessor a = (ChunkHolderAccessor) arg;
		try
		{
			a.pehkui$setCurrentlyLoading(obj);
			original.call(obj);
		}
		finally
		{
			a.pehkui$setCurrentlyLoading(null);
		}
	}
}
