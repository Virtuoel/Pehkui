package virtuoel.pehkui.mixin.compat.identity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import virtuoel.pehkui.util.ScaleUtils;

@Pseudo
@Mixin(targets = "draylar.identity.cca.IdentityComponent", remap = false)
public class IdentityComponentMixin
{
	@Shadow(remap = false) PlayerEntity player;
	@Shadow(remap = false) LivingEntity identity;
	
	@Inject(at = @At("HEAD"), method = "setIdentity", remap = false)
	private void onSetIdentity(LivingEntity identity, CallbackInfo info)
	{
		if (identity != null)
		{
			ScaleUtils.loadScale(player, identity);
		}
	}
	
	@Inject(at = @At("RETURN"), method = "fromTag", remap = false)
	private void onFromTag(CompoundTag tag, CallbackInfo info)
	{
		if (this.identity != null)
		{
			ScaleUtils.loadScale(player, identity);
		}
	}
}
