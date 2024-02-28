package io.github.projectunified.minelib.scheduler.common.util;

public class PlatformChecker {
    public static final boolean IS_PAPER;
    public static final boolean IS_FOLIA;

    static {
        boolean paper;
        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            paper = true;
        } catch (ClassNotFoundException e) {
            paper = false;
        }
        IS_PAPER = paper;

        boolean folia;
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            folia = true;
        } catch (ClassNotFoundException e) {
            folia = false;
        }
        IS_FOLIA = folia;
    }
}
