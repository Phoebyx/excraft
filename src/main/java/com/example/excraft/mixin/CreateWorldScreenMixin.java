package com.example.excraft.mixin;

import com.example.excraft.Config;
import com.example.excraft.data.ExcraftDataRegisters;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CreateWorldScreen.class)
public class CreateWorldScreenMixin {
    @Redirect(method = "openFresh", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/levelgen/presets/WorldPresets;NORMAL:Lnet/minecraft/resources/ResourceKey;",opcode = Opcodes.GETSTATIC))
    private static ResourceKey<WorldPreset> defaultWorldTypes$replaceDefault() {
        return ExcraftDataRegisters.resourceKey();
    }
}
