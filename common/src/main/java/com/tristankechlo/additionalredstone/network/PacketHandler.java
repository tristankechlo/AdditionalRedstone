package com.tristankechlo.additionalredstone.network;

import net.minecraft.server.level.ServerLevel;

public interface PacketHandler {

    void handle(ServerLevel level);

}
