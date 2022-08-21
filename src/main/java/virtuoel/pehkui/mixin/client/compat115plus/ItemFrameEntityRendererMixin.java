package virtuoel.pehkui.mixin.client.compat115plus;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.ItemFrameEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(ItemFrameEntityRenderer.class)
public class ItemFrameEntityRendererMixin
{
	@ModifyVariable(method = "render", at = @At(value = "STORE"))
	private Vec3d pehkui$render(Vec3d value, ItemFrameEntity itemFrameEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i)
	{
		final float widthScale = ScaleUtils.getBoundingBoxWidthScale(itemFrameEntity);
		final float heightScale = ScaleUtils.getBoundingBoxHeightScale(itemFrameEntity);
		
		if (widthScale != 1.0F || heightScale != 1.0F)
		{
			value = value.multiply(1.0F / widthScale, 1.0F / heightScale, 1.0F / widthScale);
			
			final Direction facing = itemFrameEntity.getHorizontalFacing();
			
			final double widthOffset = ((0.0625D - (0.03125D * widthScale)) - 0.03125D) / widthScale;
			final double heightOffset = ((0.0625D - (0.03125D * heightScale)) - 0.03125D) / heightScale;
			
			value = value.add(widthOffset * facing.getOffsetX(), heightOffset * facing.getOffsetY(), widthOffset * facing.getOffsetZ());
		}
		
		return value;
	}
}
