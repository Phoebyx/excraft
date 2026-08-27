package com.example.excraft.data;

import com.example.excraft.dimension.worldmodifiers.WorldModifier;
import com.example.excraft.dimension.worldmodifiers.WorldModifierType;
import com.example.excraft.dimension.worldmodifiers.entity.*;
import com.example.excraft.dimension.worldmodifiers.events.RainWorldModifier;
import com.example.excraft.dimension.worldmodifiers.world.EnvironmentLightLevelModifier;
import com.example.excraft.dimension.worldmodifiers.world.PermanentTimeOfDayModifier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.checkerframework.checker.units.qual.N;

import java.util.function.Supplier;

public class WorldModifierRegister {
    public static final ResourceKey<Registry<WorldModifier>> WORLD_MODIFIER_REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath("excraft", "worldmodifiers"));
    public static final ResourceKey<Registry<WorldModifierType>> WORLD_MODIFIER_TYPE_REGISTRY_KEY = net.minecraft.resources.ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath("excraft", "worldmodifiertypes"));
    public static final Registry<WorldModifier> WORLD_MODIFIER_REGISTRY = new RegistryBuilder<>(WORLD_MODIFIER_REGISTRY_KEY)
            .sync(true)
            .create();
    public static final Registry<WorldModifierType> WORLD_MODIFIER_TYPE_REGISTRY = new RegistryBuilder<>(WORLD_MODIFIER_TYPE_REGISTRY_KEY)
            .sync(true)
            .create();
    public static final DeferredRegister<WorldModifier> WORLDMODIFIERREGISTER = DeferredRegister.create(WORLD_MODIFIER_REGISTRY, "excraft");
    public static final TagKey<WorldModifier> WORLD_MODIFIER_TAG = TagKey.create(WorldModifierRegister.WORLD_MODIFIER_REGISTRY_KEY,ResourceLocation.fromNamespaceAndPath("excraft","rainworld"));
    public static final Supplier<WorldModifier> RAINWORLD = WORLDMODIFIERREGISTER.register("rainworld", RainWorldModifier::new);
    public static final Supplier<WorldModifier> SPEED = WORLDMODIFIERREGISTER.register("speed", SpeedModifier::new);
    public static final Supplier<WorldModifier> PERMANENTTIME = WORLDMODIFIERREGISTER.register("permanent_time", PermanentTimeOfDayModifier::new);
    public static final Supplier<WorldModifier> SUNBURNT = WORLDMODIFIERREGISTER.register("sunburnt", SunburntModifier::new);
    public static final Supplier<WorldModifier> ENVIRONMENT_LIGHT = WORLDMODIFIERREGISTER.register("environment_light", EnvironmentLightLevelModifier::new);
    public static final Supplier<WorldModifier> GRAVITY = WORLDMODIFIERREGISTER.register("gravity", AttributeGravityModifier::new);
    public static final Supplier<WorldModifier> STEP_HEIGHT = WORLDMODIFIERREGISTER.register("stepheight", AttributeStepHeightModifier::new);
    public static final Supplier<WorldModifier> SIZE_MATTERS = WORLDMODIFIERREGISTER.register("sizematters", SizeMattersModifier::new);




    public static void registerModifierEventListeners() {
        NeoForge.EVENT_BUS.addListener(RainWorldModifier::playerTick);
        NeoForge.EVENT_BUS.addListener(SpeedModifier::playerTick);
        NeoForge.EVENT_BUS.addListener(SunburntModifier::playerTick);
        NeoForge.EVENT_BUS.addListener(AttributeGravityModifier::entityJoin);
        NeoForge.EVENT_BUS.addListener(AttributeStepHeightModifier::entityJoin);
        NeoForge.EVENT_BUS.addListener(SizeMattersModifier::entityJoin);
    }
}
