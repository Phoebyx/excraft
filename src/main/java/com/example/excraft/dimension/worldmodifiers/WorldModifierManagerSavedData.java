package com.example.excraft.dimension.worldmodifiers;

import com.example.excraft.Excraft;
import com.example.excraft.data.ExcraftDataRegisters;
import com.example.excraft.data.WorldModifierRegister;
import com.example.excraft.dimension.ExcraftDimensionManager;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import java.util.ArrayList;
import java.util.List;

public class WorldModifierManagerSavedData extends SavedData {
    public static final String FILE_NAME = "worldmodifiersmanager";
    private List<WorldModifier> selectedModifiers = new ArrayList<>();
    private Integer salt = 0;

    public static WorldModifierManagerSavedData create() {
        return new WorldModifierManagerSavedData();
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
        CompoundTag selectedModifiersTag = new CompoundTag();
        for (WorldModifier worldModifier : this.selectedModifiers) {
            if (worldModifier != null) {
                ResourceLocation resourceLocation = WorldModifierRegister.WORLD_MODIFIER_REGISTRY.getKey(worldModifier);
                selectedModifiersTag.putString(worldModifier.getModifierName(),resourceLocation.toString());
                Excraft.LOGGER.info(selectedModifiersTag.getAsString());
            }
        }
        tag.put("SelectedModifiers",selectedModifiersTag);

        CompoundTag salt = new CompoundTag();
        if (this.salt != null) {
            salt.putInt("Salt", this.salt.intValue());
        }
        tag.put("StoredSalt", salt);

        return tag;
    }

    public static WorldModifierManagerSavedData load(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        WorldModifierManagerSavedData data = WorldModifierManagerSavedData.create();
        for (String key : tag.getAllKeys()) {
            if (key.equals("StoredSalt")) {
                CompoundTag storedSalt = tag.getCompound(key);
                data.salt = storedSalt.getInt("Salt");
            }
            if (key.equals("SelectedModifiers")) {
                CompoundTag selectedModifiersTag = tag.getCompound(key);
                for (String currentModifier : selectedModifiersTag.getAllKeys()) {
                    String resourceLocation = selectedModifiersTag.getString(currentModifier);
                    ResourceLocation location = ResourceLocation.parse(resourceLocation);
                    WorldModifier worldModifier = WorldModifierRegister.WORLD_MODIFIER_REGISTRY.get(location);
                    data.selectedModifiers.add(worldModifier);

                }
            }
        }
        return data;
    }

    public static WorldModifierManagerSavedData compute(DimensionDataStorage dataStorage) {
        return dataStorage.computeIfAbsent(new SavedData.Factory<>(WorldModifierManagerSavedData::create, WorldModifierManagerSavedData::load, null), FILE_NAME);
    }

    public void modifyData() {
        this.selectedModifiers = ExcraftDimensionManager.getCurrentManager().getCurrentModifiers();
        this.salt = ExcraftDimensionManager.getCurrentManager().getSalt();
        Excraft.LOGGER.info("Saving with " + selectedModifiers);
        this.setDirty();
    }

    public WorldModifierManager returnSavedManager(MinecraftServer server) {
        return new WorldModifierManager(
                server,
                salt,
                selectedModifiers
        );
    }
    public boolean isSaltNull() {
        return this.salt.equals(0);
    }
}
