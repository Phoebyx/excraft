package com.example.excraft.data;

import com.example.excraft.dimension.worldmodifiers.WorldModifier;
import com.example.excraft.dimension.worldmodifiers.WorldModifierType;
import com.example.excraft.dimension.worldmodifiers.entity.*;
import com.example.excraft.dimension.worldmodifiers.events.RainWorldModifier;
import com.example.excraft.dimension.worldmodifiers.world.EnvironmentLightLevelModifier;
import com.example.excraft.dimension.worldmodifiers.world.PermanentTimeOfDayModifier;
import com.example.excraft.dimension.worldmodifiers.world.worldgen.*;
import com.example.excraft.dimension.worldmodifiers.world.worldgen.noiserouter.InvertWorldModifier;
import com.example.excraft.dimension.worldmodifiers.world.worldgen.noiserouter.PrettyFlatWorldModifier;
import com.example.excraft.dimension.worldmodifiers.world.worldgen.noiserouter.SqueezeWorldModifier;
import com.example.excraft.dimension.worldmodifiers.world.worldgen.noiserouter.StepWorldModifier;
import com.example.excraft.dimension.worldmodifiers.world.worldgen.noisesettings.LavaWorldModifier;
import com.example.excraft.dimension.worldmodifiers.world.worldgen.noisesettings.SeaWorldModifier;
import com.example.excraft.dimension.worldmodifiers.world.worldgen.special.AMazeWorldModifier;
import com.example.excraft.dimension.worldmodifiers.world.worldgen.special.ChessWorldModifier;
import com.example.excraft.dimension.worldmodifiers.world.worldgen.special.RibbonWorldModifier;
import com.example.excraft.dimension.worldmodifiers.world.worldgen.special.SkyGridWorldModifier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.neoforge.common.NeoForge;
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

    public static final Supplier<WorldModifier> SPEED = WORLDMODIFIERREGISTER.register(speedModifier.getModifierResourceLocation(), () -> speedModifier);
    public static final Supplier<WorldModifier> JUMPBOOST = WORLDMODIFIERREGISTER.register(jumpBoost.getModifierResourceLocation(), () -> jumpBoost );
    public static final Supplier<WorldModifier> HUNGER = WORLDMODIFIERREGISTER.register(hunger.getModifierResourceLocation(), () -> hunger);
    public static final Supplier<WorldModifier> ABSORPTION = WORLDMODIFIERREGISTER.register(absorption.getModifierResourceLocation(), () -> absorption);

    public static final Supplier<WorldModifier> RAINWORLD = WORLDMODIFIERREGISTER.register(RainWorldModifier.modifierResourceLocation, RainWorldModifier::new);
    public static final Supplier<WorldModifier> PERMANENTTIME = WORLDMODIFIERREGISTER.register(PermanentTimeOfDayModifier.modifierResourceLocation, PermanentTimeOfDayModifier::new);
    public static final Supplier<WorldModifier> SUNBURNT = WORLDMODIFIERREGISTER.register(SunburntModifier.modifierResourceLocation, SunburntModifier::new);
    public static final Supplier<WorldModifier> ENVIRONMENT_LIGHT = WORLDMODIFIERREGISTER.register(EnvironmentLightLevelModifier.modifierResourceLocation, EnvironmentLightLevelModifier::new);
    public static final Supplier<WorldModifier> GRAVITY = WORLDMODIFIERREGISTER.register(AttributeGravityModifier.modifierResourceLocation, AttributeGravityModifier::new);
    public static final Supplier<WorldModifier> STEP_HEIGHT = WORLDMODIFIERREGISTER.register(AttributeStepHeightModifier.modifierResourceLocation, AttributeStepHeightModifier::new);
    public static final Supplier<WorldModifier> SIZE_MATTERS = WORLDMODIFIERREGISTER.register(SizeMattersModifier.modifierResourceLocation, SizeMattersModifier::new);
    public static final Supplier<WorldModifier> ENDERPEARL = WORLDMODIFIERREGISTER.register(InfiniteEnderPearlModifier.modifierResourceLocation, InfiniteEnderPearlModifier::new);
    public static final Supplier<WorldModifier> MARTIDOM = WORLDMODIFIERREGISTER.register(MartidomEntityModifier.modifierResourceLocation, MartidomEntityModifier::new);
    public static final Supplier<WorldModifier> RUSSIANDOLL = WORLDMODIFIERREGISTER.register(RussianDollEntityModifier.modifierResourceLocation, RussianDollEntityModifier::new);

    public static final Supplier<WorldGenWorldModifier> PRETTYFLAT = WORLDMODIFIERREGISTER.register(PrettyFlatWorldModifier.modifierResourceLocation, PrettyFlatWorldModifier::new);
    public static final Supplier<WorldGenWorldModifier> INVERT = WORLDMODIFIERREGISTER.register(InvertWorldModifier.modifierResourceLocation, InvertWorldModifier::new);
    public static final Supplier<WorldGenWorldModifier> SQUEEZE = WORLDMODIFIERREGISTER.register(SqueezeWorldModifier.modifierResourceLocation, SqueezeWorldModifier::new);
    public static final Supplier<WorldGenWorldModifier> SEAWORLD = WORLDMODIFIERREGISTER.register(SeaWorldModifier.modifierResourceLocation, SeaWorldModifier::new);
    public static final Supplier<WorldGenWorldModifier> STEP = WORLDMODIFIERREGISTER.register(StepWorldModifier.modifierResourceLocation, StepWorldModifier::new);
    public static final Supplier<WorldGenWorldModifier> LAVAWORLD = WORLDMODIFIERREGISTER.register(LavaWorldModifier.modifierResourceLocation, LavaWorldModifier::new);

    public static final Supplier<WorldGenWorldModifier> RIBBON = WORLDMODIFIERREGISTER.register(RibbonWorldModifier.modifierResourceLocation, RibbonWorldModifier::new);
    public static final Supplier<WorldGenWorldModifier> SKYGRID = WORLDMODIFIERREGISTER.register(SkyGridWorldModifier.modifierResourceLocation, SkyGridWorldModifier::new);
    public static final Supplier<WorldGenWorldModifier> CHESS = WORLDMODIFIERREGISTER.register(ChessWorldModifier.modifierResourceLocation, ChessWorldModifier::new);
    public static final Supplier<WorldGenWorldModifier> AMAZE = WORLDMODIFIERREGISTER.register(AMazeWorldModifier.modifierResourceLocation, AMazeWorldModifier::new);



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
