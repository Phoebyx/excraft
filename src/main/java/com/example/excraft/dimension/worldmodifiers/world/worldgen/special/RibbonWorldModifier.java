package com.example.excraft.dimension.worldmodifiers.world.worldgen.special;

import com.example.excraft.dimension.SolidWorldBorderCreator;
import com.example.excraft.dimension.worldmodifiers.WorldModifierType;
import com.example.excraft.dimension.worldmodifiers.world.WorldWorldModifierType;
import com.example.excraft.dimension.worldmodifiers.world.worldgen.WorldGenWorldModifier;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RibbonWorldModifier extends WorldWorldModifierType implements SpecialWorldGenModifier, WorldGenWorldModifier {
    private static final String modifierName = "Ribbon World";
    public static final String modifierResourceLocation = modifierName.toLowerCase().replaceAll(" ","_");
    private final int weight = 1;
    private int impact = 0;
    private static boolean active = false;
    private static String type = "specialchunkgenerator";

    @Override
    public String getModifierName() {
        return modifierName;
    }

    @Override
    public int getImpact() {
        return impact;
    }
    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public String getModifierResourceLocation() {
        return modifierResourceLocation;
    }

    @Override
    public @Nullable List<String> getDependentOn() {
        return List.of();
    }

    @Override
    public void chunkGenerator(ChunkAccess chunk) {
        int worldSize = 6;
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = chunk.getMinBuildHeight(); y < chunk.getMaxBuildHeight(); y++) {
                    if (chunk.getPos().getMinBlockX() + x >= worldSize || chunk.getPos().getMinBlockX() + x <= -worldSize) {
                        SolidWorldBorderCreator.placeWallBlock(chunk, x, y, z);
                    }
                }
            }
        }
    }

    @Override
    public String getType() {
        return type;
    }
}
