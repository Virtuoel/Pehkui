package virtuoel.pehkui.mixin.compat116minus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import virtuoel.pehkui.api.PehkuiConfig;
import virtuoel.pehkui.util.MixinConstants;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(Entity.class)
public class EntityMixin
{
	@ModifyArg(target = @Desc(value = MixinConstants.FALL, args = { double.class, boolean.class, BlockState.class, BlockPos.class }), at = @At(value = "INVOKE", desc = @Desc(owner = Block.class, value = MixinConstants.ON_LANDED_UPON, args = { World.class, BlockPos.class, Entity.class, float.class }), remap = false), remap = false)
	private float onFallModifyFallDistance(float distance)
	{
		final float scale = ScaleUtils.getFallingScale((Entity) (Object) this);
		
		if (scale != 1.0F)
		{
			if (PehkuiConfig.COMMON.scaledFallDamage.get())
			{
				return distance * scale;
			}
		}
		
		return distance;
	}
}
