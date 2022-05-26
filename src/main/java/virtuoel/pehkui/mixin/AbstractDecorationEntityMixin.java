package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.decoration.AbstractDecorationEntity;

@Mixin(AbstractDecorationEntity.class)
public abstract class AbstractDecorationEntityMixin
{
	@Shadow
	protected abstract void updateAttachmentPosition();
	
	@Inject(at = @At("RETURN"), method = "calculateDimensions")
	private void onCalculateDimensions(CallbackInfo info)
	{
		updateAttachmentPosition();
	}
}
