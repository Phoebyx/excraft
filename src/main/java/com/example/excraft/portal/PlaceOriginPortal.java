package com.example.excraft.portal;

import com.example.excraft.Excraft;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.commands.PlaceCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

import java.util.List;

public class PlaceOriginPortal {
    public StructurePieceType portalStructureLocation = BuiltInRegistries.STRUCTURE_PIECE.get(ResourceLocation.fromNamespaceAndPath("excraft","barrenrealmsportal"));
    public static final ResourceKey<Structure> PORTALSTRUCTURE = ResourceKey.create(Registries.STRUCTURE,ResourceLocation.fromNamespaceAndPath("excraft", "barrenrealmsportal"));

    public static void placeBarrenRealmPortal(MinecraftServer server, ServerStartedEvent event) {
        StructureTemplate template = server.getStructureManager().get(ResourceLocation.fromNamespaceAndPath("excraft", "barrenrealmportal")).orElseThrow();
        template.placeInWorld(
                server.overworld(),
                new BlockPos(-4,-8,-4),
                new BlockPos(0,server.overworld().getHeight(),0),
                new StructurePlaceSettings(),
                server.overworld().getRandom(),
                0
                );
    }
}
