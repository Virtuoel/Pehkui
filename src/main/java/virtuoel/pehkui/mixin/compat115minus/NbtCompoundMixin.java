package virtuoel.pehkui.mixin.compat115minus;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.nbt.NbtCompound;
import virtuoel.pehkui.util.NbtCompoundExtensions;

@Mixin(NbtCompound.class)
public abstract class NbtCompoundMixin implements NbtCompoundExtensions
{
	@Shadow(remap = false)
	abstract boolean method_10576(String key);
	@Shadow(remap = false)
	abstract UUID method_10584(String key);
	
	@Override
	public boolean pehkui_containsUuid(String key)
	{
		return method_10576(key);
	}
	
	@Override
	public UUID pehkui_getUuid(String key)
	{
		return method_10584(key);
	}
}
