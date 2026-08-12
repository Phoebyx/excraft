package com.example.excraft.dimension;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.chunk.ChunkGenerator;

import java.util.List;

public class DimensionRandomizer {

    static int salt = 7424;
    static boolean isToBeRandomized = true;

    //Return Salt
    public static int getSalt() {
        return salt;
    }

    //Manually set the salt. Prevents it from being randomized next dimension creation.
    public static void manualSalt(int inputSalt) {
        salt = inputSalt;
        isToBeRandomized = false;
    }

    //Generate a RandomSource object from salt for use on other functions.
    public static RandomSource generateRandomFromSalt() {
        return RandomSource.create(salt);
    }

    //Randomizes salt is the flag isToBeRandomized is true
    public static void randomizeSalt() {
        if (isToBeRandomized) {
            salt = (int) ((Integer.MAX_VALUE - 1) * Math.random());
        }
        isToBeRandomized = true;
    }

    //Toggles the boolean isToBeRandomized
    public static void toggleIsToBeRandomized() {
        if (isToBeRandomized) {
            isToBeRandomized = false;
        } else {
            isToBeRandomized = true;
        }
    }

    //Returns isToBeRandomized
    public static boolean isToBeRandomized() {
        return isToBeRandomized;
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
