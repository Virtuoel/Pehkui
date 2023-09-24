package virtuoel.pehkui.mixin.compat116plus.compat1201minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.world.chunk.WorldChunk;

@Mixin(ThreadedAnvilChunkStorage.class)
public abstract class ThreadedAnvilChunkStorageMixin
{
	@Redirect(method = { "method_17227", "func_219237_a_", "m_180935_", "m_202988_", "m_214854_" }, require = 0, expect = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/WorldChunk;loadToWorld()V"))
	private void pehkui$convertToFullChunk$lambda$loadEntities(WorldChunk obj, ChunkHolder arg)
	{
		final ChunkHolderAccessor a = (ChunkHolderAccessor) arg;
		try
		{
			a.pehkui$setCurrentlyLoading(obj);
			obj.loadToWorld();
		}
		finally
		{
			a.pehkui$setCurrentlyLoading(null);
		}
	}
}
