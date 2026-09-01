package com.chaosrandomizer;

// import net.minecraft.resources.ResourceLocation;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.BossEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Chaosrandomizer implements ModInitializer {
	public static final String MOD_ID = "chaosrandomizer";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static final List<ChaosEffect> EFFECTS = new ArrayList<>();
	private static final Random RANDOM = new Random();
	private static int tickCounter = 0;
	private static int nextTriggerTicks = randomInterval();

	private static final int MAX_INTERVAL_TICKS = 10 * 20; // 60 seconds (20 tick per sec)

	// bossbar gui setup
	private static ServerBossEvent bossBar;

	private static boolean active = true;



	@Override
	public void onInitialize() {
		LOGGER.info("ChaosRandomizer Initialized!");

		ChaosCommands.register();



		// Registering Modifiers yk
		EFFECTS.add(new RandomPotionEffect());
		EFFECTS.add(new ExplodingEntityEffect());
		EFFECTS.add(new SetEntityOnFireEffect());
		// EFFECTS.add(new SpawnEntity());
		// EFFECTS.add(new ThunderBolt());

		// progress bar
		bossBar = new ServerBossEvent(
				Component.literal("Next Modifier..."),
				BossEvent.BossBarColor.WHITE,
				BossEvent.BossBarOverlay.PROGRESS
		);

		ServerTickEvents.END_SERVER_TICK.register(server -> {
			// UPDATED: keep all online players attached to the bossbar

			for (ServerPlayer player : server.getPlayerList().getPlayers()) {
				if (!bossBar.getPlayers().contains(player)) {
					bossBar.addPlayer(player);

				}
			}
			tickCounter++;

			// update progress bar percentage (1.0 full, 0.0 empty)
			float progress = 1.0f - ((float) tickCounter / MAX_INTERVAL_TICKS);
			bossBar.setProgress(Math.max(0.0f, progress));

			// trigger chaos event when timer reaches max
			if (tickCounter >= MAX_INTERVAL_TICKS) {
				tickCounter = 0;
				nextTriggerTicks = randomInterval();

				if (!EFFECTS.isEmpty()) {
					ChaosEffect effect = EFFECTS.get(RANDOM.nextInt(EFFECTS.size()));
					bossBar.setName(Component.literal("Chaos: " + effect.getName()));
					for (ServerPlayer player : server.getPlayerList().getPlayers()) {
						// apply effect logic
						effect.apply(player);

						// Plays sound at the player's exact location in the world
						player.level().playSound(
								null, // null so the player themselves can also hear it
								player.getX(),
								player.getY(),
								player.getZ(),
								SoundEvents.GENERIC_EXPLODE.value(),
								SoundSource.MASTER,
								1.0f,
								1.0f
						);

					}
				}
			}
		});
	}

	private static int randomInterval() {
		// 40s to 80s interval (in ticks)
		return (2 + RANDOM.nextInt(3)) * 20; // for testing make it shorter
	}

	public static void setActive(boolean isActive) {
		active = isActive;
	}

	public static boolean isActive() {
		return active;
	}}


//	public static ResourceLocation id(String path) {
//		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
//	}
//}