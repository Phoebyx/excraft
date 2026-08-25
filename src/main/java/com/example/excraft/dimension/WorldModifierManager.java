package com.example.excraft.dimension;

import com.example.excraft.data.ExcraftTimer;
import com.example.excraft.dimension.worldmodifiers.WorldModifier;
import com.example.excraft.dimension.worldmodifiers.WorldModifierType;
import com.example.excraft.dimension.worldmodifiers.events.RainWorldModifier;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.*;

public class WorldModifierManager {
    private MinecraftServer minecraftServer;
    private RandomSource randomSource;
    private int currentModifierSlots;
    private long currentTime;
    private List<WorldModifier> currentModifiers;

    public WorldModifierManager(MinecraftServer server, int currentModifierSlots) {
        this.minecraftServer = server;
        this.randomSource = DimensionRandomizer.generateRandomFromSalt();
        this.currentModifierSlots = currentModifierSlots;
        this.currentTime = retrieveCurrentTime(server);
        this.currentModifiers = rollModifiers();
    }

    public List<WorldModifier> rollModifiers() {
        return List.of(new RainWorldModifier());
    }

    private long retrieveCurrentTime(MinecraftServer server) {
        if (server == null) {return 0;}
        else return currentTime;
    }

    //private NavigableMap<Double,WorldModifier> {

    //}

    public void worldModifierScheduler() {
        for (WorldModifier worldModifier : currentModifiers) {
            worldModifier.activateEffect(minecraftServer, ExcraftDimensionManager.EXCRAFT_LEVEL);
        }
    }

    private List<ResourceKey<WorldModifier>> getWorldModifierList() {
        RegistryAccess registryAccess = minecraftServer.registryAccess();
        Registry<WorldModifier> worldModifiers = registryAccess.registryOrThrow(WorldModifierRegister.WORLD_MODIFIER_REGISTRY_KEY);
        List<ResourceKey<WorldModifier>> list = new ArrayList<>();
        for (Map.Entry<ResourceKey<WorldModifier>, WorldModifier> entry : worldModifiers.entrySet()) {
            list.addLast(entry.getKey());
        }
        return list;
    }
}
