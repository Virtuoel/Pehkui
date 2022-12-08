package virtuoel.pehkui.util;

import java.util.Arrays;
import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

import com.google.common.reflect.Reflection;

import virtuoel.pehkui.Pehkui;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.api.ScaleTypes;

public class BackwardsCompatibility
{
	private static boolean APPLIED = false;
	
	private static final List<String> TYPES = Arrays.asList(
		"INVALID", "BASE", "WIDTH", "HEIGHT", "EYE_HEIGHT", "HITBOX_WIDTH",
		"HITBOX_HEIGHT", "MODEL_WIDTH", "MODEL_HEIGHT", "THIRD_PERSON", "MOTION",
		"FALLING", "STEP_HEIGHT", "VIEW_BOBBING", "FLIGHT", "REACH", "BLOCK_REACH",
		"ENTITY_REACH", "KNOCKBACK", "ATTACK", "DEFENSE", "HEALTH", "DROPS",
		"HELD_ITEM", "PROJECTILES", "EXPLOSIONS"
	);
	
	public static void addFieldsIfNeeded(final ClassNode targetClass)
	{
		if (VersionUtils.MINOR < 18 || (VersionUtils.MINOR == 18 && VersionUtils.PATCH == 0))
		{
			for (final String type : TYPES)
			{
				targetClass.visitField(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, type, "L" + targetClass.name + ";", null, null);
			}
			APPLIED = true;
			Pehkui.LOGGER.debug("[Pehkui] Applied backwards compatibility patch.");
		}
	}
	
	public static void populateFieldsIfNeeded()
	{
		if (APPLIED)
		{
			Reflection.initialize(ScaleTypes.class);
			for (final String type : TYPES)
			{
				try
				{
					ScaleType.class.getField(type).set(null, ScaleTypes.class.getField(type).get(null));
				}
				catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e)
				{
					Pehkui.LOGGER.catching(e);
					Pehkui.LOGGER.warn("[Pehkui] Backwards compatibility patch failed. Older mods you have that use Pehkui might not work.");
					break;
				}
			}
			Pehkui.LOGGER.debug("[Pehkui] Populated backwards compatibility fields.");
		}
	}
}
