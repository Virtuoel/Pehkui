package virtuoel.pehkui.mixin.compat117plus.compat1201minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.WorldChunk;
import virtuoel.pehkui.mixin.compat116plus.compat1201minus.ChunkHolderAccessor;

@Mixin(value = ServerChunkManager.class, priority = 1010)
public abstract class ServerChunkManagerMixin
{
	@Shadow
	abstract ChunkHolder getChunkHolder(long pos);
	
	@Inject(method = "getChunk", require = 0, expect = 0, at = @At(value = "INVOKE", shift = Shift.BEFORE, target = "Lnet/minecraft/util/profiler/Profiler;visit(Ljava/lang/String;)V"), cancellable = true)
	private void pehkui$getChunk$visit(int x, int z, ChunkStatus leastStatus, boolean create, CallbackInfoReturnable<Chunk> info)
	{
		final ChunkHolderAccessor h = (ChunkHolderAccessor) getChunkHolder(ChunkPos.toLong(x, z));
		if (h != null)
		{
			final WorldChunk c = h.pehkui$getCurrentlyLoading();
			if (c != null)
			{
				info.setReturnValue(c);
			}
		}
	}
}
