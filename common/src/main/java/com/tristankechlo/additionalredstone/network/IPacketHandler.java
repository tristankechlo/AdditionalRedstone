package com.tristankechlo.additionalredstone.network;

import net.minecraft.server.level.ServerLevel;

public interface IPacketHandler {

    void handle(ServerLevel level);

}
