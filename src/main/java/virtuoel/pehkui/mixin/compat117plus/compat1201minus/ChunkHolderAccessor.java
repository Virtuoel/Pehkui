package virtuoel.pehkui.mixin.compat117plus.compat1201minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.server.world.ChunkHolder;
import net.minecraft.world.chunk.WorldChunk;

@Mixin(ChunkHolder.class)
public interface ChunkHolderAccessor
{
	@Accessor("currentlyLoading")
	WorldChunk pehkui$getCurrentlyLoading();
	
	@Accessor("currentlyLoading")
	void pehkui$setCurrentlyLoading(WorldChunk chunk);
}
