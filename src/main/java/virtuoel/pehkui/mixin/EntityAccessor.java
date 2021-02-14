package virtuoel.pehkui.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.Entity;

@Mixin(Entity.class)
public interface EntityAccessor
{
	@Accessor("onGround")
	boolean getOnGround();
	
	@Accessor("onGround")
	void setOnGround(boolean onGround);
}
