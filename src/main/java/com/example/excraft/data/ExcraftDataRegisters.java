package com.example.excraft.data;

import com.example.excraft.Config;
import com.example.excraft.Excraft;
import com.example.excraft.blocks.ExcraftBlocks;
import com.example.excraft.blocks.ExcraftPortalBlock;
import com.example.excraft.dimension.WorldModifierManager;
import com.example.excraft.dimension.WorldModifierRegister;
import com.example.excraft.dimension.worldmodifiers.WorldModifier;
import com.example.excraft.items.ExcraftItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NewRegistryEvent;

import java.util.concurrent.CompletableFuture;

public class ExcraftDataRegisters {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Excraft.MODID);

    public static void registerRegisters(IEventBus modEventBus) {
        DeferredRegister<?>[] registers = registers();
        for (DeferredRegister<?> register: registers){
            register.register(modEventBus);
        }
    }
    public static void registerRegistries(NewRegistryEvent event) {
        Registry<?>[] registries = registries();
        for (Registry<?> registry: registries) {
            event.register(registry);
        }
    }
    private static Registry<?>[] registries() {
        Registry<?>[] registries = {
                WorldModifierRegister.WORLD_MODIFIER_REGISTRY,
                WorldModifierRegister.WORLD_MODIFIER_TYPE_REGISTRY
        };
        return registries;
    }
    private static DeferredRegister<?>[] registers() {
         DeferredRegister<?>[] registers = {
                 ExcraftBlocks.BLOCKS,
                 ExcraftItems.ITEMS,
                 ExcraftCreativeModeTab.CREATIVE_MODE_TABS,
                 ExcraftPortalBlock.ATTACHMENT_TYPES
        };
        return registers;
    }

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
    }

    public static ResourceKey<WorldPreset> resourceKey() {
        if (Config.REPLACE_WORLD_SCREEN.get()) {
            return ResourceKey.create(Registries.WORLD_PRESET, ResourceLocation.fromNamespaceAndPath("excraft", "barrenrealms"));
        } else {
            return WorldPresets.NORMAL;
        }
    }
}
/*  */