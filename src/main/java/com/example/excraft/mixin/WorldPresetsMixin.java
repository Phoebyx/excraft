package com.example.excraft.mixin;

import com.example.excraft.Config;
import com.example.excraft.data.ExcraftDataRegisters;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldPresets.class)
public class WorldPresetsMixin {
    @Redirect(method = {"createNormalWorldDimensions","getNormalOverworld"},
            require = 2,
            at = @At(value = "FIELD",opcode = Opcodes.GETSTATIC, target = "Lnet/minecraft/world/level/levelgen/presets/WorldPresets;NORMAL:Lnet/minecraft/resources/ResourceKey;"))
    private static ResourceKey<WorldPreset> defaultWorldTypes$replaceDefault() {
        return ExcraftDataRegisters.resourceKey();
    }
}
