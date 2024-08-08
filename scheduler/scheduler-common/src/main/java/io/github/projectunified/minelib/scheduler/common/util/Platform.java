package io.github.projectunified.minelib.scheduler.common.util;

import java.util.function.BooleanSupplier;

/**
 * The platform the server is running on
 */
public enum Platform {
    FOLIA("io.papermc.paper.threadedregions.RegionizedServer"),
    PAPER("com.destroystokyo.paper.PaperConfig"),
    BUKKIT(() -> true); // Default platform

    private final boolean isPlatform;

    Platform(BooleanSupplier isPlatformCheck) {
        this.isPlatform = isPlatformCheck.getAsBoolean();
    }

    Platform(String className) {
        this(() -> {
            try {
                Class.forName(className);
                return true;
            } catch (ClassNotFoundException e) {
                return false;
            }
        });
    }

    /**
     * Check if this platform can be applied
     *
     * @return true if this platform can be applied
     */
    public boolean isPlatform() {
        return isPlatform;
    }
}
