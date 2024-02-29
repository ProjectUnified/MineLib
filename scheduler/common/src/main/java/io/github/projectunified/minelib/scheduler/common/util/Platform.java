package io.github.projectunified.minelib.scheduler.common.util;

import java.util.function.BooleanSupplier;

public enum Platform {
    FOLIA(() -> {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }),
    PAPER(() -> {
        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }),
    BUKKIT(() -> true);

    public static final Platform CURRENT;

    static {
        Platform current = null;
        for (Platform platform : values()) {
            if (platform.isPlatform) {
                current = platform;
                break;
            }
        }
        if (current == null) {
            throw new IllegalStateException("Unknown platform");
        }
        CURRENT = current;
    }

    private final boolean isPlatform;

    Platform(BooleanSupplier isPlatformCheck) {
        this.isPlatform = isPlatformCheck.getAsBoolean();
    }

    public boolean isPlatform() {
        return isPlatform;
    }
}
