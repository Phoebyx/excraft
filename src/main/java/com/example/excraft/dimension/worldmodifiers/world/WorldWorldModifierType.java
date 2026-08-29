package com.example.excraft.dimension.worldmodifiers.world;

import com.example.excraft.dimension.worldmodifiers.WorldModifier;
import com.example.excraft.dimension.worldmodifiers.WorldModifierType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.DensityFunction;

public abstract class WorldWorldModifierType implements WorldModifierType {
    public WorldWorldModifierType() {
        roll();
    }

    public DensityFunction modifyWorld(DensityFunction function) {
        return null;
    }

    public void activateEffect(MinecraftServer server, ResourceKey<Level> levelResourceKey) {

    }

    public void disabledEffect(MinecraftServer server, ResourceKey<Level> levelResourceKey) {

    }
    public void roll() {

    }

}
