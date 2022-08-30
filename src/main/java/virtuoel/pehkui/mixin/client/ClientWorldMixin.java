package virtuoel.pehkui.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import virtuoel.pehkui.util.ScaleRenderUtils;

@Mixin(ClientWorld.class)
public class ClientWorldMixin
{
	@Inject(method = "addDetailsToCrashReport", at = @At(value = "RETURN"))
	private void pehkui$addDetailsToCrashReport(CrashReport report, CallbackInfoReturnable<CrashReportSection> info)
	{
		ScaleRenderUtils.addDetailsToCrashReport(info.getReturnValue());
	}
}
