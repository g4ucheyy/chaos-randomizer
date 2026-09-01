package com.chaosrandomizer;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;

import java.util.Random;

public class RandomPotionEffect implements ChaosEffect {
    private static final Random RANDOM = new Random();

    /*
    * Adding more pots eff and longer dur
    * Maybe to mobs as well?
    *
    * */


    // potion effects
    @SuppressWarnings("unchecked")
    private static final Holder<MobEffect>[] POOL = new Holder[] {
            MobEffects.SPEED,
            MobEffects.SLOWNESS,
            MobEffects.LEVITATION,
            MobEffects.NAUSEA,
            MobEffects.BLINDNESS,
            MobEffects.JUMP_BOOST,
            MobEffects.GLOWING,
            MobEffects.REGENERATION,
            MobEffects.HEALTH_BOOST,
            MobEffects.POISON,
            MobEffects.SLOWNESS
    };

    @Override
    public void apply(ServerPlayer player) {
        Holder<MobEffect> chosen = POOL[RANDOM.nextInt(POOL.length)];
        int amplifier = RANDOM.nextInt(3);

        // apply to players
        player.addEffect(new MobEffectInstance(chosen, 600, amplifier));
        player.sendSystemMessage(Component.literal("Modifier: " + getName()));

        // OPTIONAL: APPLIED TO ALL NEARBY MOBS WITHIN 10 BLOCK RADIUS
        player.level().getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(10.0)).forEach(entity -> {
           entity.addEffect(new MobEffectInstance(chosen, 600, amplifier));
        });
    }

    @Override
    public String getName() {
        return "Random Potion Effect!";
    }
}