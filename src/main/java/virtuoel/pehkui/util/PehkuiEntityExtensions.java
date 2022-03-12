package virtuoel.pehkui.util;

import java.util.Map;

import net.minecraft.nbt.NbtCompound;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;

public interface PehkuiEntityExtensions
{
	ScaleData pehkui_constructScaleData(ScaleType type);
	
	ScaleData pehkui_getScaleData(ScaleType type);
	
	Map<ScaleType, ScaleData> pehkui_getScales();
	
	boolean pehkui_shouldSyncScales();
	
	void pehkui_setShouldSyncScales(boolean sync);
	
	void pehkui_readScaleNbt(NbtCompound nbt);
	
	void pehkui_writeScaleNbt(NbtCompound nbt);
	
	boolean pehkui_getOnGround();
	
	void pehkui_setOnGround(boolean onGround);
}
