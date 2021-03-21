package virtuoel.pehkui.mixin.identity.compat115;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import virtuoel.pehkui.util.ScaleUtils;

@Pseudo
@Mixin(targets = "draylar.identity.cca.IdentityComponent", remap = false)
public class IdentityComponentMixin
{
	@Shadow(remap = false) PlayerEntity player;
	@Shadow(remap = false) LivingEntity identity;
	
	@Inject(at = @At("RETURN"), method = "fromTag", remap = false)
	private void onFromTag(NbtCompound tag, CallbackInfo info)
	{
		if (this.identity != null)
		{
			ScaleUtils.loadScale(player, identity);
		}
	}
}
