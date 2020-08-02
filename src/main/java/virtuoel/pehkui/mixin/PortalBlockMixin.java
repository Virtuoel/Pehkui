package virtuoel.pehkui.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import net.minecraft.block.BlockState;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import virtuoel.pehkui.api.PehkuiConfig;
import virtuoel.pehkui.util.PehkuiBlockStateExtensions;

@Mixin(NetherPortalBlock.class)
public abstract class PortalBlockMixin
{
	@Inject(at = @At("HEAD"), method = "onEntityCollision", cancellable = true)
	private void onOnEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo info)
	{
		if (Optional.ofNullable(PehkuiConfig.DATA.get("accurateNetherPortals"))
			.filter(JsonElement::isJsonPrimitive).map(JsonElement::getAsJsonPrimitive)
			.filter(JsonPrimitive::isBoolean).map(JsonPrimitive::getAsBoolean)
			.orElse(true))
		{
			if (!entity.getBoundingBox().intersects(((PehkuiBlockStateExtensions) state).pehkui_getOutlineShape(world, pos).getBoundingBox().offset(pos)))
			{
				info.cancel();
			}
		}
	}
}
