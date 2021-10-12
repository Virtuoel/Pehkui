package virtuoel.pehkui.command;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.EntitySelectorReader;
import net.minecraft.predicate.NumberRange.FloatRange;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import virtuoel.pehkui.Pehkui;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.api.ScaleTypes;
import virtuoel.pehkui.command.argument.ScaleTypeArgumentType;
import virtuoel.pehkui.mixin.EntitySelectorOptionsInvoker;
import virtuoel.pehkui.util.CommandUtils;
import virtuoel.pehkui.util.I18nUtils;
import virtuoel.pehkui.util.PehkuiEntitySelectorReaderExtensions;

public class PehkuiEntitySelectorOptions
{
	public static final Text SCALE_RANGE_DESCRIPTION = I18nUtils.translate("argument.entity.options." + Pehkui.MOD_ID + ".scale_range.description", "Entities with scale value");
	public static final Text SCALE_TYPE_DESCRIPTION = I18nUtils.translate("argument.entity.options." + Pehkui.MOD_ID + ".scale_type.description", "Entities with scale type");
	
	public static void register()
	{
		EntitySelectorOptionsInvoker.callPutOption(
			Pehkui.id("scale").toString().replace(':', '.'),
			r -> cast(r).pehkui_setScaleRange(FloatRange.parse(r.getReader())),
			r -> cast(r).pehkui_getScaleRange().isDummy(),
			SCALE_RANGE_DESCRIPTION
		);
		
		EntitySelectorOptionsInvoker.callPutOption(
			Pehkui.id("scale_type").toString().replace(':', '.'),
			r -> cast(r).pehkui_setScaleType(parseScaleType(r)),
			r -> cast(r).pehkui_getScaleType() == ScaleTypes.INVALID,
			SCALE_TYPE_DESCRIPTION
		);
		
		EntitySelectorOptionsInvoker.callPutOption(
			Pehkui.id("computed_scale").toString().replace(':', '.'),
			r -> cast(r).pehkui_setComputedScaleRange(FloatRange.parse(r.getReader())),
			r -> cast(r).pehkui_getComputedScaleRange().isDummy(),
			SCALE_RANGE_DESCRIPTION
		);
		
		EntitySelectorOptionsInvoker.callPutOption(
			Pehkui.id("computed_scale_type").toString().replace(':', '.'),
			r -> cast(r).pehkui_setComputedScaleType(parseScaleType(r)),
			r -> cast(r).pehkui_getComputedScaleType() == ScaleTypes.INVALID,
			SCALE_TYPE_DESCRIPTION
		);
	}
	
	private static PehkuiEntitySelectorReaderExtensions cast(EntitySelectorReader reader)
	{
		return ((PehkuiEntitySelectorReaderExtensions) reader);
	}
	
	private static ScaleType parseScaleType(EntitySelectorReader reader) throws CommandSyntaxException
	{
		reader.setSuggestionProvider((builder, consumer) ->
		{
			CommandUtils.suggestIdentifiersIgnoringNamespace(
				Pehkui.MOD_ID,
				ScaleRegistries.SCALE_TYPES.keySet(),
				builder
			);
			
			return builder.buildFuture();
		});
		
		final int i = reader.getReader().getCursor();
		
		final Identifier id = Identifier.fromCommandInput(reader.getReader());
		final ScaleType scaleType = ScaleRegistries.getEntry(ScaleRegistries.SCALE_TYPES, id);
		
		final Identifier defaultId = ScaleRegistries.getDefaultId(ScaleRegistries.SCALE_TYPES);
		final ScaleType defaultType = ScaleRegistries.getEntry(ScaleRegistries.SCALE_TYPES, defaultId);
		
		if (scaleType == null || (scaleType == defaultType && !id.equals(defaultId)))
		{
			reader.getReader().setCursor(i);
			throw ScaleTypeArgumentType.INVALID_ENTRY_EXCEPTION.createWithContext(reader.getReader(), id.toString());
		}
		
		return scaleType;
	}
}
