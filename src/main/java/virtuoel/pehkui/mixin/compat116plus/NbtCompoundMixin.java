package virtuoel.pehkui.mixin.compat116plus;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.nbt.NbtCompound;
import virtuoel.pehkui.util.NbtCompoundExtensions;

@Mixin(NbtCompound.class)
public abstract class NbtCompoundMixin implements NbtCompoundExtensions
{
	@Shadow
	abstract boolean containsUuid(String key);
	@Shadow
	abstract UUID getUuid(String key);
	
	@Override
	public boolean pehkui_containsUuid(String key)
	{
		return containsUuid(key);
	}
	
	@Override
	public UUID pehkui_getUuid(String key)
	{
		return getUuid(key);
	}
}
