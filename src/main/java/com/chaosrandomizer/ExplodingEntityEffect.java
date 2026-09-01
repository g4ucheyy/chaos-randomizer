package com.chaosrandomizer;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Random;

    public class ExplodingEntityEffect implements ChaosEffect {
        private static final Random RANDOM = new Random();
        private static final double SEARCH_RADIUS = 10.0;

        @Override
        public void apply(ServerPlayer player) {
            List<LivingEntity> nearbyMobs = player.level().getEntitiesOfClass(
                    LivingEntity.class,
                    player.getBoundingBox().inflate(SEARCH_RADIUS),
                    entity -> !(entity instanceof ServerPlayer)
            );

            // If no mobs are around, skip the explosion (do not target the player)
            if (nearbyMobs.isEmpty()) {
                player.sendSystemMessage(Component.literal("Modifier: Big Boom! (No mobs nearby, Skipping...)"));
                return;
            }

            // pick a random nearby mob to blow up; fall back to the player if alone
            LivingEntity targetMob = nearbyMobs.get(RANDOM.nextInt(nearbyMobs.size()));

            // explosion at the target's location -  block damage, no fire
            player.level().explode(
                    null, // source entity - null so it's not attributed as player-caused
                    targetMob.getX(),
                    targetMob.getY(),
                    targetMob.getZ(),
                    2.5f, // power - damage + knockback, small radius
                    false, // no fire
                    Level.ExplosionInteraction.BLOCK//  block breaking
            );

            player.sendSystemMessage(Component.literal("Modifier: " + getName()));
        }

        @Override
        public String getName() {
            return "Big Boom!";
        }
    }

