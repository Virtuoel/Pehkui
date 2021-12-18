package virtuoel.pehkui.util;

import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import com.google.common.base.CaseFormat;

import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import virtuoel.pehkui.Pehkui;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.api.ScaleType;

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
				Identifier id;
				String namespace, path, capitalized;
				
				for (final Entry<Identifier, ScaleType> entry : ScaleRegistries.SCALE_TYPES.entrySet())
				{
					id = entry.getKey();
					namespace = id.getNamespace();
					
					if (namespace.equals(Pehkui.MOD_ID))
					{
						final ScaleType type = entry.getValue();
						path = id.getPath();
						
						final BiFunction<Entity, Float, Float> getScale = (e, d) -> type.getScaleData(e).getBaseScale(d);
						final BiFunction<Entity, Float, Float> computeScale = (e, d) -> type.getScaleData(e).getScale(d);
						final BiConsumer<Entity, Float> setScale = (e, s) ->
						{
							final ScaleData d = type.getScaleData(e);
							d.setScale(s);
							d.setBaseScale(s);
						};
						
						capitalized = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, path);
						
						ReflectionUtils.setField(interfaceClass, "get" + capitalized + "Scale", null, getScale);
						ReflectionUtils.setField(interfaceClass, "compute" + capitalized + "Scale", null, computeScale);
						ReflectionUtils.setField(interfaceClass, "set" + capitalized + "Scale", null, setScale);
					}
				}
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
