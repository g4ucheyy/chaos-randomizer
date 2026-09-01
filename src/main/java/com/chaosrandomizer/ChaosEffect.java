package com.chaosrandomizer;

import net.minecraft.server.level.ServerPlayer;

public interface ChaosEffect {

    void apply(ServerPlayer player);
    String getName();
}
