package virtuoel.pehkui.util;

import net.minecraft.block.AbstractBlock.AbstractBlockState;
import net.minecraft.block.BlockState;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.particle.ItemPickupParticle;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.entity.BoatEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.ItemFrameEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.command.EntitySelectorOptions;
import net.minecraft.command.EntitySelectorReader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.ai.brain.task.VillagerBreedTask;
import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.mob.EndermiteEntity;
import net.minecraft.entity.mob.EvokerEntity;
import net.minecraft.entity.mob.EvokerFangsEntity;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.entity.mob.PatrolEntity;
import net.minecraft.entity.mob.RavagerEntity;
import net.minecraft.entity.mob.SilverfishEntity;
import net.minecraft.entity.mob.SkeletonHorseEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.passive.AbstractDonkeyEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.DragonFireballEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.projectile.LlamaSpitEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.StorageMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.screen.HorseScreenHandler;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.world.explosion.Explosion;

public class MixinTargetClasses
{
	public static class Common
	{
		public static final Class<?>[] CLASSES =
		{
			AbstractBlockState.class, ////
			AbstractDecorationEntity.class,
			AbstractDonkeyEntity.class,
			AbstractFurnaceBlockEntity.class,
			AbstractMinecartEntity.class,
			AbstractSkeletonEntity.class,
			AnimalEntity.class,
			AnimalMateGoal.class,
			ArmorStandEntity.class,
			BlazeEntity.class, //
			BlockState.class,
			BoatEntity.class,
			BrewingStandBlockEntity.class,
			DragonFireballEntity.class,
			EggEntity.class,
			EndCrystalEntity.class,
			EnderChestBlockEntity.class,
			EnderPearlEntity.class,
			EndermiteEntity.class,
			Entity.class,
			EntitySelectorOptions.class,
			EntitySelectorReader.class,
			EntityTrackerEntry.class,
			EvokerEntity.class, //
			EvokerFangsEntity.class,
			Explosion.class,
			ExplosiveProjectileEntity.class,
			FallingBlockEntity.class,
			FireballEntity.class,
			FishingBobberEntity.class,
			FlyingEntity.class,
			FoxEntity.class,
			HorseBaseEntity.class,
			HorseScreenHandler.class,
			Item.class,
			ItemEntity.class,
			LivingEntity.class,
			LlamaEntity.class,
			LlamaSpitEntity.class,
			LootableContainerBlockEntity.class,
			NetherPortalBlock.class,
			OtherClientPlayerEntity.class,
			PassiveEntity.class,
			PatrolEntity.class,
			PersistentProjectileEntity.class,
			PigEntity.class,
			PlayerEntity.class,
			PlayerManager.class,
			ProjectileEntity.class, ////
			ProjectileUtil.class,
			RavagerEntity.class,
			ServerPlayNetworkHandler.class,
			ServerPlayerEntity.class,
			ServerPlayerInteractionManager.class,
			SilverfishEntity.class,
			SkeletonHorseEntity.class,
			SlimeEntity.class,
			SpawnEggItem.class,
			SpiderEntity.class,
			StorageMinecartEntity.class,
			ThrownEntity.class,
			TntEntity.class,
			VillagerBreedTask.class,
			VillagerEntity.class,
			ZombieEntity.class,
			ZombieVillagerEntity.class,
		};
	}
	
	public static class Client
	{
		public static final Class<?>[] CLASSES =
		{
			BoatEntityRenderer.class,
			Camera.class,
			ClientPlayNetworkHandler.class,
			ClientPlayerEntity.class,
			ClientPlayerInteractionManager.class,
			EntityRenderDispatcher.class,
			EntityRenderer.class,
			GameRenderer.class,
			HeldItemRenderer.class,
			InGameHud.class,
			InGameOverlayRenderer.class,
			InventoryScreen.class,
			ItemFrameEntityRenderer.class,
			ItemPickupParticle.class,
			PlayerEntityRenderer.class,
		};
	}
	
	public static class Server
	{
		public static final Class<?>[] CLASSES =
		{
		};
	}
}
