package virtuoel.pehkui.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import java.util.Map;
import java.util.Optional;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import virtuoel.pehkui.Pehkui;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.server.command.DebugCommand;
import virtuoel.pehkui.util.PehkuiEntityExtensions;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(Entity.class)
public abstract class EntityMixin implements PehkuiEntityExtensions {
	@Shadow
	boolean onGround;
	@Shadow
	boolean firstUpdate;

	private boolean pehkui_shouldSyncScales = false;
	private boolean pehkui_shouldIgnoreScaleNbt = false;
	private ScaleData[] pehkui_scaleCache = null;

	@Override
	public ScaleData pehkui_constructScaleData(ScaleType type) {
		return ScaleData.Builder.create().type(type).entity((Entity) (Object) this).build();
	}

	@Override
	public ScaleData[] pehkui_getScaleCache() {
		return pehkui_scaleCache;
	}

	@Override
	public void pehkui_setScaleCache(ScaleData[] scaleCache) {
		pehkui_scaleCache = scaleCache;
	}

	@Override
	public void pehkui_setShouldSyncScales(boolean sync) {
		pehkui_shouldSyncScales = sync;
	}

	@Override
	public boolean pehkui_shouldSyncScales() {
		return pehkui_shouldSyncScales;
	}

	@Override
	public boolean pehkui_shouldIgnoreScaleNbt() {
		return pehkui_shouldIgnoreScaleNbt;
	}

	@Override
	public void pehkui_setShouldIgnoreScaleNbt(boolean ignore) {
		pehkui_shouldIgnoreScaleNbt = ignore;
	}

	@Inject(at = @At("HEAD"), method = "readData")
	private void pehkui$readData(ReadView view, CallbackInfo info) {
		if (pehkui_shouldIgnoreScaleNbt()) {
			return;
		}
		Optional<ReadView> scaleDataViewOpt = view.getOptionalReadView(Pehkui.MOD_ID + ":scale_data_types");
		if (scaleDataViewOpt.isPresent()) {
			ReadView scaleDataView = scaleDataViewOpt.get();
			for (Map.Entry<Identifier, ScaleType> entry : ScaleRegistries.SCALE_TYPES.entrySet()) {
				String key = entry.getKey().toString();
				Optional<ReadView> typeViewOpt = scaleDataView.getOptionalReadView(key);
				if (typeViewOpt.isPresent()) {
					ReadView typeView = typeViewOpt.get();
					ScaleData scaleData = pehkui_getScaleData(entry.getValue());

					scaleData.setBaseScale(typeView.getFloat("scale", scaleData.getScaleType().getDefaultBaseScale()));
					scaleData.setTargetScale(
						typeView.getFloat("target", scaleData.getScaleType().getDefaultBaseScale()));
					scaleData.setScaleTickDelay(
						typeView.getInt("total_ticks", scaleData.getScaleType().getDefaultTickDelay()));
					scaleData.setPersistence(typeView.getBoolean("persistent", false));

					scaleData.onUpdate();
				}
			}

			// Convert ReadView to NbtCompound for DebugCommand
			NbtCompound nbt = new NbtCompound();
			view.getOptionalString("UUID").ifPresent(uuid -> nbt.putString("UUID", uuid));
			// Add other fields if needed

			DebugCommand.unmarkEntityForScaleReset((Entity) (Object) this, nbt);
		}
	}

	@Override
	public void pehkui_readScaleNbt(NbtCompound nbt) {
		if (pehkui_shouldIgnoreScaleNbt()) {
			return;
		}

		if (nbt.contains(Pehkui.MOD_ID + ":scale_data_types")) {
			DebugCommand.unmarkEntityForScaleReset((Entity) (Object) this, nbt);
		}
	}

	private void pehkui_writeScaleDataToView(WriteView view) {
		if (pehkui_shouldIgnoreScaleNbt()) {
			return;
		}
		WriteView typeDataView = view.get(Pehkui.MOD_ID + ":scale_data_types");
		for (final ScaleData scaleData : pehkui_getScales().values()) {
			if (scaleData != null) {
				WriteView scaleTypeView = typeDataView.get(
					ScaleRegistries.getId(ScaleRegistries.SCALE_TYPES, scaleData.getScaleType()).toString()
				);
				scaleTypeView.putFloat("scale", scaleData.getBaseScale());
				scaleTypeView.putFloat("target", scaleData.getTargetScale());
				scaleTypeView.putInt("total_ticks", scaleData.getScaleTickDelay());
				scaleTypeView.putBoolean("persistent", scaleData.shouldPersist());
			}
		}
	}

	@Inject(at = @At("HEAD"), method = "writeData")
	private void pehkui$writeData(WriteView view, CallbackInfo info) {
		pehkui_writeScaleDataToView(view);
	}

	@Override
	public NbtCompound pehkui_writeScaleNbt(NbtCompound nbt) {
		if (pehkui_shouldIgnoreScaleNbt()) {
			return nbt;
		}

		final NbtCompound typeData = new NbtCompound();

		NbtCompound compound;
		for (final ScaleData scaleData : pehkui_getScales().values()) {
			if (scaleData != null) {
				compound = scaleData.writeNbt(new NbtCompound());

				if (compound.getSize() != 0) {
					typeData.put(
						ScaleRegistries.getId(ScaleRegistries.SCALE_TYPES, scaleData.getScaleType()).toString(),
						compound);
				}
			}
		}

		if (typeData.getSize() > 0) {
			nbt.put(Pehkui.MOD_ID + ":scale_data_types", typeData);
		}

		return nbt;
	}

	@Inject(at = @At("HEAD"), method = "tick")
	private void pehkui$tick(CallbackInfo info) {
		for (ScaleType type : ScaleRegistries.SCALE_TYPES.values()) {
			ScaleUtils.tickScale(pehkui_getScaleData(type));
		}
	}

	@ModifyReturnValue(method = "getDimensions", at = @At("RETURN"))
	private EntityDimensions pehkui$getDimensions(EntityDimensions original) {
		final float widthScale = ScaleUtils.getBoundingBoxWidthScale((Entity) (Object) this);
		final float heightScale = ScaleUtils.getBoundingBoxHeightScale((Entity) (Object) this);

		if (widthScale != 1.0F || heightScale != 1.0F) {
			return original.scaled(widthScale, heightScale);
		}

		return original;
	}

	@Inject(at = @At("HEAD"), method = "onStartedTrackingBy")
	private void pehkui$onStartedTrackingBy(ServerPlayerEntity player, CallbackInfo info) {
		ScaleUtils.syncScalesOnTrackingStart((Entity) (Object) this, player.networkHandler);
	}

	@ModifyVariable(method = "dropStack(Lnet/minecraft/item/ItemStack;F)Lnet/minecraft/entity/ItemEntity;",
		at = @At(value = "STORE"))
	private ItemEntity pehkui$dropStack(ItemEntity entity) {
		ScaleUtils.setScaleOfDrop(entity, (Entity) (Object) this);
		return entity;
	}

	@ModifyExpressionValue(method = "move", at = @At(value = "CONSTANT", args = "doubleValue=1.0E-7D"))
	private double pehkui$move$minVelocity(double value) {
		final float scale = ScaleUtils.getMotionScale((Entity) (Object) this);

		return scale < 1.0F ? scale * scale * value : value;
	}

	@ModifyArg(method = "move", index = 0, at = @At(value = "INVOKE",
		target = "Lnet/minecraft/entity/Entity;adjustMovementForSneaking(Lnet/minecraft/util/math/Vec3d;" +
			"Lnet/minecraft/entity/MovementType;)Lnet/minecraft/util/math/Vec3d;"))
	private Vec3d pehkui$move$adjustMovementForSneaking(Vec3d movement, MovementType type) {
		if (type == MovementType.SELF || type == MovementType.PLAYER) {
			return movement.multiply(ScaleUtils.getMotionScale((Entity) (Object) this));
		}

		return movement;
	}

	@WrapOperation(method = "pushAwayFrom",
		at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/entity/Entity;addVelocity(DDD)V"))
	private void pehkui$pushSelfAwayFrom$other(Entity obj, double x, double y, double z, Operation<Void> original,
											   @Local(argsOnly = true) Entity other) {
		final float otherScale = ScaleUtils.getMotionScale(other);

		if (otherScale != 1.0F) {
			x *= otherScale;
			z *= otherScale;
		}

		original.call(obj, x, y, z);
	}

	@WrapOperation(method = "pushAwayFrom",
		at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/entity/Entity;addVelocity(DDD)V"))
	private void pehkui$pushSelfAwayFrom$self(Entity obj, double x, double y, double z, Operation<Void> original) {
		final float ownScale = ScaleUtils.getMotionScale((Entity) (Object) this);

		if (ownScale != 1.0F) {
			x *= ownScale;
			z *= ownScale;
		}

		original.call(obj, x, y, z);
	}

	@Inject(at = @At("HEAD"), method = "spawnSprintingParticles", cancellable = true)
	private void pehkui$spawnSprintingParticles(CallbackInfo info) {
		if (ScaleUtils.getMotionScale((Entity) (Object) this) < 1.0F) {
			info.cancel();
		}
	}

	@Override
	public boolean pehkui_isFirstUpdate() {
		return this.firstUpdate;
	}

	@Override
	public boolean pehkui_getOnGround() {
		return this.onGround;
	}

	@Override
	public void pehkui_setOnGround(boolean onGround) {
		this.onGround = onGround;
	}
}
