package com.example.excraft.dimension.worldmodifiers.world.worldgen.special;

import com.example.excraft.dimension.worldmodifiers.world.worldgen.WorldGenWorldModifier;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.jetbrains.annotations.Nullable;

public interface SpecialWorldGenModifier extends WorldGenWorldModifier {
    public void chunkGenerator(ChunkAccess chunk);
}
