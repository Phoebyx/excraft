package com.example.excraft;

import com.example.excraft.data.ExcraftTimer;
import com.example.excraft.dimension.DimensionRandomizer;
import com.example.excraft.dimension.ExcraftDimensionManager;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.Set;

public class ExcraftCommands {

    public static boolean debugInfo = false;

    public static void onRegisterCommands(RegisterCommandsEvent event)
    {
        event.getDispatcher().register(Commands.literal("excraft")
                .then(Commands.literal("createDimension")
                        .executes(context -> {
                            MinecraftServer server = context.getSource().getServer();
                            Excraft.createDimension(server);
                            return 1;
                        }))
                .then(Commands.literal("deleteDimension")
                        .executes(context -> {
                            MinecraftServer server = context.getSource().getServer();
                            Excraft.deleteDimension(server);
                            return 1;
                        }))
                .then(Commands.literal("setSeed")
                        .then(Commands.argument("seed", IntegerArgumentType.integer())
                                .executes(context -> {
                                    DimensionRandomizer.manualSalt(IntegerArgumentType.getInteger(context,"seed"));
                                    return 1;
                                }))
                )
                .then(Commands.literal("readSeed")
                        .executes(ExcraftCommands::readSeed)
                )
                .then(Commands.literal("tp")
                        .executes(ExcraftCommands::tpPlayerToExcraft)
                )
                .then(Commands.literal("timer")
                        .executes(ExcraftCommands::timer)
                )
                .then(Commands.literal("testSalt")
                        .executes(ExcraftCommands::testSalt)
                )
                .then(Commands.literal("toggleDebugInfo")
                        .executes(context -> {debugInfo = !debugInfo; return 1;})
                )
        );
    }

    private static int readSeed(CommandContext<CommandSourceStack> context) {
        int salt = DimensionRandomizer.getSalt();
        context.getSource().sendSuccess(
                () -> Component.literal(String.valueOf(salt)),
                true
        );
        return 1;
    }

    private static int timer(CommandContext<CommandSourceStack> context) {
        MinecraftServer server = context.getSource().getServer();
        context.getSource().sendSuccess(
                () -> Component.literal(ExcraftTimer.getCurrentTimerInHumanReadableForm(server)),
                true
        );
        return 1;
    }

    private static int testSalt(CommandContext<CommandSourceStack> context) {
        RandomSource randomSource = DimensionRandomizer.generateRandomFromSalt();
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            number.append(randomSource.nextIntBetweenInclusive(0, 5));
        }
        context.getSource().sendSuccess(
                () -> Component.literal(number.toString()),
                true
        );
        return 1;
    }

    private static int tpPlayerToExcraft(CommandContext<CommandSourceStack> context) {
        MinecraftServer server = context.getSource().getServer();
        Player target = context.getSource().getPlayer();
        if (target.level() != server.getLevel(ExcraftDimensionManager.EXCRAFT_LEVEL)) {
            target.teleportTo(server.getLevel(ExcraftDimensionManager.EXCRAFT_LEVEL), target.getX(),target.getY(),target.getZ(), Set.of(),0,0);
        } else {
            target.teleportTo(server.overworld(), target.getX(),target.getY(),target.getZ(),Set.of(),0,0);
        }
        return 1;
    }
}
