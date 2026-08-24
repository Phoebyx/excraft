package com.example.excraft.data;

import com.example.excraft.Excraft;
import com.example.excraft.blocks.ExcraftBlocks;
import com.example.excraft.items.ExcraftItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ExcraftCreativeModeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Excraft.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXCRAFT_TAB = CREATIVE_MODE_TABS.register("excraft_tab", () -> net.minecraft.world.item.CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.excraft")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> new ItemStack(ExcraftBlocks.UNBREAKABLE_CHISELED_SANDSTONE.get()))
            .displayItems((parameters, output) -> {
                output.accept(ExcraftBlocks.UNBREAKABLE_SANDSTONE.get());
                output.accept(ExcraftBlocks.UNBREAKABLE_SANDSTONE_STAIRS.get());
                output.accept(ExcraftBlocks.UNBREAKABLE_SANDSTONE_SLABS.get());
                output.accept(ExcraftBlocks.UNBREAKABLE_CHISELED_SANDSTONE.get());
                output.accept(ExcraftItems.CACTUS_AXE.get());
                // Add the example item to the tab. For your own tabs, this method is preferred over the event
            }).build()
    );
    public static final void addCreative(BuildCreativeModeTabContentsEvent event) {
        ResourceKey<CreativeModeTab> tab = event.getTabKey();
    }
}


