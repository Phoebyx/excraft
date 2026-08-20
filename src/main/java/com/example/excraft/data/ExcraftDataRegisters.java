package com.example.excraft.data;

import com.example.excraft.Config;
import com.example.excraft.Excraft;
import com.example.excraft.blocks.DisabledDimensionBlocksTag;
import com.example.excraft.blocks.ExcraftBlocks;
import com.example.excraft.blocks.ExcraftPortalBlock;
import com.example.excraft.dimension.DisabledDimensionBlocks;
import com.example.excraft.items.ExcraftItems;
import net.minecraft.DetectedVersion;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.util.InclusiveRange;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ExcraftDataRegisters {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Excraft.MODID);

    public static void registerRegisters(IEventBus modEventBus) {
        DeferredRegister<?>[] registers = registers();
        for (DeferredRegister<?> register: registers){
            register.register(modEventBus);
        }
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