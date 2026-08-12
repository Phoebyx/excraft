package com.example.excraft.dimension;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.chunk.ChunkGenerator;

import java.util.List;

public class DimensionRandomizer {
    static long salt = 7424;
    static int minInt =

    public static long generateSalt() {
        return salt;
    }

    public static long getSalt() {
        return salt;
    }

    public static void manualSalt(int inputSalt) {
        salt = inputSalt;
    }

    public static RandomSource generateRandomFromSalt() {
        return RandomSource.create(salt);
    }
    public static void randomizeSalt() {
        salt = Math.
    }

    /*    public void dimensionKick(MinecraftServer server) {
        PlayerList playerList = server.getPlayerList();
        List<ServerPlayer> player = playerList.getPlayers();
        int i = 0;
        while (player.size() > i) {
            i++;
            if (player.get(i).level() == ) {

            }
        }
    }
*/

    public boolean hasSkyLight() {
        return hasCeiling();
    }

    //Detect from noise
    public boolean hasCeiling() {
        return true;
    }

    public boolean isUltraWarm() {
        return true;
    }

    public boolean isNatural() {
        return true;
    }

    public float getAmbientLight() {
        return 0.0F;
    }
}
