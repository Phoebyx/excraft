package com.example.excraft;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.Tags;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    private static final List<String> defaultDisabledWorkstations = List.of(
            "c:player_workstations/crafting_table",
            "c:player_workstations/furnaces",
            "c:player_workstations/crafting_tables"
    );
    private static final List<String> defaultDisabledNoises = List.of(
            "minecraft:nether",
            "minecraft:end"
    );
    public static final ModConfigSpec.ConfigValue<Boolean> REPLACE_WORLD_SCREEN = BUILDER
            .comment("Make Barren Realms the default world preset")
            .define("worldpresetbool",true);

    public static final ModConfigSpec.ConfigValue<List<? extends String>> DISABLED_WORKSTATIONS_IN_DIMENSION = BUILDER
            .comment("The list of workstations and blocks to disable placement of in the roguelike dimension")
            .defineListAllowEmpty("items", defaultDisabledWorkstations, () -> "", Config::validateName);

    public static final ModConfigSpec.ConfigValue<Double> WORLD_CYCLE = BUILDER
            .comment("How long, in hours, should each dimension last for. Accepts decimal inputs for minutes.")
            .defineInRange("worldcycle",(double) 1,0, 99999999);

    public static final ModConfigSpec.ConfigValue<Long> PORTAL_COLOR_TICK_CYCLE = BUILDER
            .comment("How many ticks should it take to update the portal colour based on the current timer.")
            .defineInRange("portaltickcycle", 360L,20,99999999);

    public static final ModConfigSpec.ConfigValue<Long> TPCOOLDOWN = BUILDER
            .comment("How long, in seconds, should the tp cooldown be")
            .defineInRange("tpcooldown",(long) 10,0, 99999999);

    public static final ModConfigSpec.ConfigValue<List<? extends String>> NOISE_BLACKLIST = BUILDER
            .comment("Prevent these dimensions from appearing")
            .defineListAllowEmpty("noiseblacklist", defaultDisabledNoises, () -> "", Config::validateName);

    static final ModConfigSpec SPEC = BUILDER.build();

    private static boolean validateItemName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }

    private static boolean validateBlockName(final Object obj) {
        return obj instanceof String blockName && (BuiltInRegistries.BLOCK.containsKey(ResourceLocation.parse(blockName)));
    }
    private static boolean validateName(final Object obj) {
        return obj instanceof String name;
    }
}
