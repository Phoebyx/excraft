package com.example.excraft.dimension.worldmodifiers.world.worldgen.noiserouter;

import com.example.excraft.dimension.worldmodifiers.world.WorldWorldModifierType;
import com.example.excraft.dimension.worldmodifiers.world.worldgen.WorldGenWorldModifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PrettyFlatWorldModifier extends WorldWorldModifierType implements WorldGenWorldModifier {
    private static final String modifierName = "Pretty Flat";
    public static final String modifierResourceLocation = modifierName.toLowerCase().replaceAll(" ","_");
    private final int weight = 1;
    private int impact = 0;
    private static ResourceKey<Level> levelResourceKey;
    private static boolean active = false;
    private static String type = "densityfunction";

    public String getType() {
        return type;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public String getModifierName() {
        return modifierName;
    }

    @Override
    public String getModifierResourceLocation() {
        return modifierResourceLocation;
    }

    @Override
    public int getImpact() {
        return impact;
    }

    @Override
    public @Nullable List<String> getDependentOn() {
        return List.of();
    }

    @Override
    public void activateEffect(MinecraftServer server, ResourceKey<Level> levelResourceKey) {
        active = true;
    }

    @Override
    public void disabledEffect(MinecraftServer server, ResourceKey<Level> levelResourceKey) {
        active = false;
    }

    @Override
    public DensityFunction modifyWorld(DensityFunction toModify) {
        //return DensityFunctions.mul(toModify.clamp(-0.1,0.1),DensityFunctions.constant(0.001));
        return DensityFunctions.yClampedGradient(-64,320,-1,1);
    }
}
