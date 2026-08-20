package com.example.excraft.portal;

import com.example.excraft.dimension.ExcraftDimensionManager;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;

public class PlaceOriginPortal {
    public StructurePieceType portalStructureLocation = BuiltInRegistries.STRUCTURE_PIECE.get(ResourceLocation.fromNamespaceAndPath("excraft","barrenrealmsportal"));
    public static final ResourceKey<Structure> PORTALSTRUCTURE = ResourceKey.create(Registries.STRUCTURE,ResourceLocation.fromNamespaceAndPath("excraft", "barrenrealmsportal"));

    public static void placeBarrenRealmPortal(MinecraftServer server) {
        StructureTemplate template = server.getStructureManager().get(ResourceLocation.fromNamespaceAndPath("excraft", "barrenrealmportal")).orElseThrow();
        template.placeInWorld(
                server.overworld(),
                new BlockPos(-4,-8,-4),
                new BlockPos(0,0,0),
                new StructurePlaceSettings(),
                server.overworld().getRandom(),
                0
                );
    }
    public static void placeExitPortal(MinecraftServer server,int y) {
        StructureTemplate template = server.getStructureManager().get(ResourceLocation.fromNamespaceAndPath("excraft", "barrenrealmportal")).orElseThrow();
        StructurePlaceSettings structurePlaceSettings = new StructurePlaceSettings();
        template.placeInWorld(
                server.getLevel(ExcraftDimensionManager.EXCRAFT_LEVEL),
                new BlockPos(-4,y - 8,-4),
                new BlockPos(0,0,0),
                structurePlaceSettings,
                server.overworld().getRandom(),
                1
        );
    }
}
