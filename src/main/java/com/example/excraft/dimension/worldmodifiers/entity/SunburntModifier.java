package com.example.excraft.dimension.worldmodifiers.entity;

import com.example.excraft.dimension.ExcraftDimensionManager;
import com.example.excraft.dimension.worldmodifiers.WorldModifier;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SunburntModifier extends EntityWorldModifierType implements WorldModifier {
    private static final String modifierName = "Sunburnt";
    private static final ResourceLocation modifierResourceLocation = ResourceLocation.fromNamespaceAndPath("excraft","sunburnt");
    private final int weight = 1;
    private final int impact = -4;
    private static ResourceKey<Level> levelResourceKey;
    private static boolean active = false;

    @Override
    public int getWeight() {
        return weight;
    }

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
        active = true;
    }

    @Override
    public void disabledEffect(MinecraftServer server, ResourceKey<Level> levelResourceKey) {
        active = false;
    }

    @SubscribeEvent
    public static void eventListener(PlayerTickEvent.Post event) {
        Entity currentEntity = event.getEntity();
        if (burnCheck(event,currentEntity)) {
            currentEntity.igniteForSeconds(3);
        }
    }

    private static boolean burnCheck(PlayerTickEvent.Post event, Entity currentEntity) {
        Level level = currentEntity.level();
        BlockPos posAbove = currentEntity.blockPosition().above();
        BlockState aboveBlock = event.getEntity().level().getBlockState(currentEntity.blockPosition().above());
        return  level.dimension() == ExcraftDimensionManager.EXCRAFT_LEVEL
                && active
                && level.getDayTime() >= 0
                && level.getDayTime() <= 12000
                && !level.isRaining()
                && (currentEntity.level().canSeeSky(currentEntity.blockPosition())
                || (!aboveBlock.isViewBlocking(level,posAbove)
                && level.canSeeSky(posAbove.above())
                ));
    }
}
