package com.example.excraft.data;

import com.example.excraft.dimension.worldmodifiers.WorldModifier;
import com.example.excraft.dimension.worldmodifiers.WorldModifierType;
import com.example.excraft.dimension.worldmodifiers.entity.*;
import com.example.excraft.dimension.worldmodifiers.events.RainWorldModifier;
import com.example.excraft.dimension.worldmodifiers.world.EnvironmentLightLevelModifier;
import com.example.excraft.dimension.worldmodifiers.world.PermanentTimeOfDayModifier;
import com.example.excraft.dimension.worldmodifiers.world.worldgen.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public class WorldModifierRegister {
    private static PotionEffectModifier speedModifier = new PotionEffectModifier(5,"Speed",MobEffects.MOVEMENT_SPEED,1);
    private static PotionEffectModifier hunger = new PotionEffectModifier(-6,"Hunger",MobEffects.HUNGER,0);
    private static PotionEffectModifier absorption = new PotionEffectModifier(3,"Absorption",MobEffects.ABSORPTION,4);
    private static PotionEffectModifier jumpBoost = new PotionEffectModifier(5,"Jump Boost",MobEffects.JUMP,1);

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
    public static final Supplier<WorldModifier> SPEED = WORLDMODIFIERREGISTER.register("speed", () -> speedModifier);
    public static final Supplier<WorldModifier> JUMPBOOST = WORLDMODIFIERREGISTER.register("jump_boost", () -> jumpBoost );
    public static final Supplier<WorldModifier> HUNGER = WORLDMODIFIERREGISTER.register("hunger", () -> hunger);
    public static final Supplier<WorldModifier> ABSORPTION = WORLDMODIFIERREGISTER.register("absorption", () -> absorption);
    public static final Supplier<WorldModifier> PERMANENTTIME = WORLDMODIFIERREGISTER.register("permanent_time", PermanentTimeOfDayModifier::new);
    public static final Supplier<WorldModifier> SUNBURNT = WORLDMODIFIERREGISTER.register("sunburnt", SunburntModifier::new);
    public static final Supplier<WorldModifier> ENVIRONMENT_LIGHT = WORLDMODIFIERREGISTER.register("environment_light", EnvironmentLightLevelModifier::new);
    public static final Supplier<WorldModifier> GRAVITY = WORLDMODIFIERREGISTER.register("gravity", AttributeGravityModifier::new);
    public static final Supplier<WorldModifier> STEP_HEIGHT = WORLDMODIFIERREGISTER.register("stepheight", AttributeStepHeightModifier::new);
    public static final Supplier<WorldModifier> SIZE_MATTERS = WORLDMODIFIERREGISTER.register("sizematters", SizeMattersModifier::new);
    public static final Supplier<WorldModifier> ENDERPEARL = WORLDMODIFIERREGISTER.register("infiniteenderpearl", InfiniteEnderPearlModifier::new);
    public static final Supplier<WorldModifier> MARTIDOM = WORLDMODIFIERREGISTER.register("martidom", MartidomEntityModifier::new);
    public static final Supplier<WorldModifier> RUSSIANDOLL = WORLDMODIFIERREGISTER.register("russiandoll", RussianDollEntityModifier::new);

    public static final Supplier<WorldGenWorldModifier> PRETTYFLAT = WORLDMODIFIERREGISTER.register("prettyflat", PrettyFlatWorldModifier::new);
    public static final Supplier<WorldGenWorldModifier> INVERT = WORLDMODIFIERREGISTER.register("invert", InvertWorldModifier::new);
    public static final Supplier<WorldGenWorldModifier> SQUEEZE = WORLDMODIFIERREGISTER.register("squeeze", SqueezeWorldModifier::new);
    public static final Supplier<WorldGenWorldModifier> SEAWORLD = WORLDMODIFIERREGISTER.register("seaworld", SeaWorldModifier::new);
    public static final Supplier<WorldGenWorldModifier> STEP = WORLDMODIFIERREGISTER.register("step", StepWorldModifier::new);
    public static final Supplier<WorldGenWorldModifier> LAVAWORLD = WORLDMODIFIERREGISTER.register("lavaworld", LavaWorldModifier::new);


    public static void registerModifierEventListeners() {
        NeoForge.EVENT_BUS.addListener(RainWorldModifier::eventListener);
        NeoForge.EVENT_BUS.addListener(speedModifier::eventListener);
        NeoForge.EVENT_BUS.addListener(jumpBoost::eventListener);
        NeoForge.EVENT_BUS.addListener(hunger::eventListener);
        NeoForge.EVENT_BUS.addListener(absorption::eventListener);
        NeoForge.EVENT_BUS.addListener(SunburntModifier::eventListener);
        NeoForge.EVENT_BUS.addListener(AttributeGravityModifier::eventListener);
        NeoForge.EVENT_BUS.addListener(AttributeStepHeightModifier::eventListener);
        NeoForge.EVENT_BUS.addListener(SizeMattersModifier::eventListener);
        NeoForge.EVENT_BUS.addListener(InfiniteEnderPearlModifier::eventListener);
        NeoForge.EVENT_BUS.addListener(MartidomEntityModifier::eventListener);
        NeoForge.EVENT_BUS.addListener(RussianDollEntityModifier::eventListener);
        NeoForge.EVENT_BUS.addListener(RussianDollEntityModifier::eventListenerForDrops);

    }

}
