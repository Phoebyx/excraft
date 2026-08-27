package com.example.excraft.dimension.worldmodifiers.world;

import com.example.excraft.dimension.DimensionRandomizer;
import com.example.excraft.dimension.worldmodifiers.WorldModifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnvironmentLightLevelModifier extends WorldWorldModifierType implements WorldModifier {
    private static final String modifierName = "Environment Light";
    private static final ResourceLocation modifierResourceLocation = ResourceLocation.fromNamespaceAndPath("excraft","environment_light");
    private final int weight = 1;
    private int impact = 0;
    private static ResourceKey<Level> levelResourceKey;
    private static boolean active = false;
    private static long lightLevel = -1;

    public EnvironmentLightLevelModifier() {
        rollLight();
    }

    private void rollLight() {
        lightLevel = DimensionRandomizer.generateRandomFromSalt().nextIntBetweenInclusive(0,15);
        impact = Math.toIntExact(Math.round(2 * Math.sqrt((double) lightLevel) - 3.0F));
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
    public ResourceLocation getModifierResourceLocation() {
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
        server.getLevel(levelResourceKey);
        server.getPlayerList();
        /*
        for (Player player: server.getPlayerList().getPlayers()) {
            player.getLoa
        }*/
    }

    @Override
    public void disabledEffect(MinecraftServer server, ResourceKey<Level> levelResourceKey) {
        rollLight();
    }
}
