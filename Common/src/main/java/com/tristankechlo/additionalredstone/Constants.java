package com.tristankechlo.additionalredstone;

import com.tristankechlo.additionalredstone.init.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ServiceLoader;

public class Constants {

    public static final String MOD_NAME = "AdditionalRedstone";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
    public static final String MOD_ID = "additionalredstone";
    public static final String GITHUB_URL = "https://github.com/tristankechlo/AdditionalRedstone";
    public static final String GITHUB_ISSUE_URL = GITHUB_URL + "/issues";
    public static final String GITHUB_WIKI_URL = GITHUB_URL + "/wiki";
    public static final String DISCORD_URL = "https://discord.gg/bhUaWhq";
    public static final String CURSEFORGE_URL = "https://curseforge.com/minecraft/mc-mods/additional-redstone";
    public static final String MODRINTH_URL = "https://modrinth.com/mod/additional-redstone";
    public static final int TEXT_COLOR_SCREEN = 0xCCCCCC;

    public static void registerContent() {
        ModBlockEntities.load();
        ModBlocks.load();
        ModContainer.load();
        ModItems.load();
        ModItemGroups.load();
    }

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        LOGGER.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }

}
