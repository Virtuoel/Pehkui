package virtuoel.pehkui.util;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import net.minecraft.entity.Entity;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleTypes;

public class ImmersivePortalsCompatibility
{
	private static final boolean IMMERSIVE_PORTALS_LOADED = ModLoaderUtils.isModLoaded("imm_ptl_core") || ModLoaderUtils.isModLoaded("immersive_portals");
	
	public static final ImmersivePortalsCompatibility INSTANCE = new ImmersivePortalsCompatibility();
	
	private boolean enabled;
	
	private ImmersivePortalsCompatibility()
	{
		this.enabled = IMMERSIVE_PORTALS_LOADED;
		
		if (this.enabled)
		{
			final Optional<Class<?>> interfaceClass = ReflectionUtils.getClass(
				"qouteall.imm_ptl.core.PehkuiInterface",
				"com.qouteall.immersive_portals.PehkuiInterface"
			);
			
			if (interfaceClass.isPresent())
			{
				final BiFunction<Entity, Float, Float> getThirdPersonScale = (e, d) -> ScaleTypes.THIRD_PERSON.getScaleData(e).getScale(d);
				final Function<Entity, Float> getBlockReachScale = e -> ScaleTypes.BLOCK_REACH.getScaleData(e).getScale();
				final Function<Entity, Float> getMotionScale = e -> ScaleTypes.MOTION.getScaleData(e).getScale();
				final Function<Entity, Float> getBaseScale = e -> ScaleTypes.BASE.getScaleData(e).getBaseScale();
				final BiConsumer<Entity, Float> setBaseScale = (e, s) ->
				{
					final ScaleData d = ScaleTypes.BASE.getScaleData(e);
					d.setScale(s);
					d.setBaseScale(s);
				};
				
				ReflectionUtils.setField(interfaceClass, "getThirdPersonScale", null, getThirdPersonScale);
				ReflectionUtils.setField(interfaceClass, "getBlockReachScale", null, getBlockReachScale);
				ReflectionUtils.setField(interfaceClass, "getMotionScale", null, getMotionScale);
				ReflectionUtils.setField(interfaceClass, "getBaseScale", null, getBaseScale);
				ReflectionUtils.setField(interfaceClass, "setBaseScale", null, setBaseScale);
			}
			
			final Optional<Class<?>> globalClass = ReflectionUtils.getClass(
				"qouteall.imm_ptl.core.IPGlobal",
				"qouteall.imm_ptl.core.Global",
				"com.qouteall.immersive_portals.Global"
			);
			
			if (globalClass.isPresent())
			{
				ReflectionUtils.setField(globalClass, "enableDepthClampForPortalRendering", null, true);
			}
		}
	}
}
