package com.example.excraft.data;

import com.example.excraft.Config;
import com.example.excraft.blocks.ExcraftPortalTint;
import com.example.excraft.dimension.ExcraftDimensionManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.logging.Level;

public class ExcraftTimer {
    private static int lastColorIndexSelected = 0;

    public static long getCurrentTimer(MinecraftServer server) {
        long currentTimePassed = server.getLevel(ExcraftDimensionManager.EXCRAFT_LEVEL).getGameTime() % timerInTicks();
        return timerInTicks() - currentTimePassed;
    }

    public static String getCurrentTimerInHumanReadableForm(MinecraftServer server) {
        if (server.getLevel(ExcraftDimensionManager.EXCRAFT_LEVEL) == null) {
            return "The dimension doesn't exist yet";
        }
        long currentTimerSeconds = getCurrentTimer(server)/20;
        long hours = currentTimerSeconds/3600;
        long minutes =(currentTimerSeconds % 3600) / 60;
        long seconds = currentTimerSeconds % 60;
        String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        return new String("Current Timer:" + "\nIn Ticks: " + getCurrentTimer(server) + "\nIn Hours:Minutes:Seconds: " + timeString);
    }

    public static long timerInTicks() {
        return (long) (Config.WORLD_CYCLE.get() * 72000);
    }
    public static void updateLastColorIndexSelected(int colorIndexSelected) {
        lastColorIndexSelected = colorIndexSelected;
    }

    public static int intPortalBlockColor(ServerLevel level) {
        int coloursSize = ExcraftPortalTint.getCurrentColour().size();
        float currentTimerColourNoClamp = (float)  coloursSize * ExcraftTimer.getCurrentTimer(level.getServer()) / ExcraftTimer.timerInTicks();
        int colorIndexSelected = Math.clamp( (int) currentTimerColourNoClamp, 0 , coloursSize);
        return colorIndexSelected;
    }
    public static boolean isLastColorIndexSelectedSameAsLast(int colorIndexSelected, ServerTickEvent event) {
        boolean portalCheckCycleTime = event.getServer().overworld().getGameTime() % Config.PORTAL_COLOR_TICK_CYCLE.get() == 1;
        return lastColorIndexSelected != colorIndexSelected && portalCheckCycleTime;
    }
}
