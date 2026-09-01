package com.chaosrandomizer;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

import java.util.Random;

public class SetEntityOnFireEffect implements ChaosEffect {
    private static final Random RANDOM = new Random();
    private static final double SEARCH_RADIUS = 10.0;

    @Override
    public void apply(ServerPlayer player) {
        int seconds = 3 + RANDOM.nextInt(6); // 3-8 seconds

        // apply to the player
        player.setRemainingFireTicks(seconds * 20);
        player.sendSystemMessage(Component.literal("Modifier: " + getName()));

        // OPTIONAL: apply to all nearby mobs within 10 block radius
        player.level().getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(SEARCH_RADIUS))
                .forEach(entity -> entity.setRemainingFireTicks(seconds * 20));
    }

    @Override
    public String getName() {
        return "Hot in Herre";
    }
}