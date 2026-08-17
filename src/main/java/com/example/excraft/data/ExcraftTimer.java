package com.example.excraft.data;

import com.example.excraft.Config;
import com.example.excraft.dimension.DimensionManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

public class ExcraftTimer {
    public static long getCurrentTimer(MinecraftServer server) {
        long currentTimePassed = server.getLevel(DimensionManager.EXCRAFT_LEVEL).getGameTime();
        return timerInTicks() - currentTimePassed;
    }

    public static String getCurrentTimerInHumanReadableForm(MinecraftServer server) {
        if (server.getLevel(DimensionManager.EXCRAFT_LEVEL) == null) {
            return "The dimension doesn't exist yet";
        }
        long currentTimerSeconds = getCurrentTimer(server)/20;
        long hours = currentTimerSeconds/3600;
        long minutes =(currentTimerSeconds % 3600) / 60;
        long seconds = currentTimerSeconds % 60;
        String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        return new String("Current Timer:" + "\nIn Ticks: " + getCurrentTimer(server) + "\nIn Hours:Minutes:Seconds: " + timeString);
    }
    private static long timerInTicks() {
        return (long) (Config.WORLD_CYCLE.get() * 72000);
    }
}
