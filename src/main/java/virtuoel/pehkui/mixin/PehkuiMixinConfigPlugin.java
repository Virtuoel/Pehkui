package virtuoel.pehkui.mixin;

import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import virtuoel.pehkui.util.ModLoaderUtils;
import virtuoel.pehkui.util.VersionUtils;

public class PehkuiMixinConfigPlugin implements IMixinConfigPlugin
{
	private static final String MIXIN_PACKAGE = "virtuoel.pehkui.mixin";
	
	@Override
	public void onLoad(String mixinPackage)
	{
		if (!mixinPackage.startsWith(MIXIN_PACKAGE))
		{
			throw new IllegalArgumentException(
				String.format("Invalid package: Expected \"%s\", but found \"%s\".", MIXIN_PACKAGE, mixinPackage)
			);
		}
	}
	
	@Override
	public String getRefMapperConfig()
	{
		return null;
	}
	
	private static final boolean REACH_ATTRIBUTES_LOADED = ModLoaderUtils.isModLoaded("reach-entity-attributes");
	private static final boolean STEP_HEIGHT_ATTRIBUTES_LOADED = ModLoaderUtils.isModLoaded("step-height-entity-attribute");
	private static final boolean IDENTITY_LOADED = ModLoaderUtils.isModLoaded("identity");
	private static final boolean MAGNA_LOADED = ModLoaderUtils.isModLoaded("magna");
	private static final boolean OPTIFABRIC_LOADED = ModLoaderUtils.isModLoaded("optifabric");
	public static final boolean APOTHIC_ATTRIBUTES_LOADED = ModLoaderUtils.isModLoaded("attributeslib"); 
	
	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName)
	{
		if (!mixinClassName.startsWith(MIXIN_PACKAGE))
		{
			throw new IllegalArgumentException(
				String.format("Invalid package for class \"%s\": Expected \"%s\", but found \"%s\".", targetClassName, MIXIN_PACKAGE, mixinClassName)
			);
		}
		
		if (!VersionUtils.shouldApplyCompatibilityMixin(mixinClassName))
		{
			return false;
		}
		
		if (mixinClassName.endsWith("InGameOverlayRendererMixin"))
		{
			return OPTIFABRIC_LOADED == mixinClassName.contains(".optifine.compat.");
		}
		else if (mixinClassName.endsWith("IForgeEntityMixin"))
		{
			return true;
		}
		else if (mixinClassName.endsWith("IForgePlayerMixin"))
		{
			return true;
		}
		else if (mixinClassName.equals(MIXIN_PACKAGE + ".reach.client.ClientPlayerInteractionManagerMixin") || mixinClassName.equals(MIXIN_PACKAGE + ".reach.client.GameRendererMixin"))
		{
			return false;
		}
		
		if (mixinClassName.startsWith(MIXIN_PACKAGE + ".reach"))
		{
			return REACH_ATTRIBUTES_LOADED == mixinClassName.contains(".reach.compat.");
		}
		else if (mixinClassName.startsWith(MIXIN_PACKAGE + ".step_height"))
		{
			return STEP_HEIGHT_ATTRIBUTES_LOADED == mixinClassName.contains(MIXIN_PACKAGE + ".step_height.compat.");
		}
		else if (mixinClassName.startsWith(MIXIN_PACKAGE + ".identity.compat"))
		{
			return IDENTITY_LOADED;
		}
		else if (mixinClassName.startsWith(MIXIN_PACKAGE + ".magna.compat"))
		{
			return MAGNA_LOADED;
		}
		
		return true;
	}
	
	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets)
	{
		
	}
	
	@Override
	public List<String> getMixins()
	{
		return null;
	}
	
	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo)
	{
		
	}
	
	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo)
	{
		
	}
}
