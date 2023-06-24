package com.tristankechlo.additionalredstone;

import java.nio.file.Path;
import java.util.ServiceLoader;

public interface IPlatformHelper {

    public static final IPlatformHelper INSTANCE = load(IPlatformHelper.class);

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Constants.LOGGER.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }

    Path getConfigDirectory();

}
