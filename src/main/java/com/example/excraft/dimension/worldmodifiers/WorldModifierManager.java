package com.example.excraft.dimension.worldmodifiers;

import com.example.excraft.Config;
import com.example.excraft.Excraft;
import com.example.excraft.data.WorldModifierRegister;
import com.example.excraft.dimension.DimensionRandomizer;
import com.example.excraft.dimension.ExcraftDimensionManager;
import com.example.excraft.dimension.worldmodifiers.world.worldgen.WorldGenWorldModifier;
import com.example.excraft.dimension.worldmodifiers.world.worldgen.special.SpecialWorldGenModifier;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RandomSource;

import java.util.*;

public class WorldModifierManager {
    private MinecraftServer minecraftServer;
    private RandomSource randomSource;
    private int currentModifierSlots;
    private long currentTime;
    private List<WorldModifier> currentModifiers;
    private int salt;
    private boolean disabled;
    private List<WorldGenWorldModifier> worldGenModifiers = new ArrayList<>();

    public WorldModifierManager(MinecraftServer server, int currentModifierSlots) {
        this.minecraftServer = server;
        this.randomSource = DimensionRandomizer.generateRandomFromSalt();
        this.salt = DimensionRandomizer.getSalt();
        this.currentModifierSlots = currentModifierSlots;
        this.currentTime = retrieveCurrentTime(server);
        this.currentModifiers = rollModifiers();
        this.disabled = false;
    }

    public WorldModifierManager(MinecraftServer server, int salt, List<WorldModifier> currentModifiers) {
        this.minecraftServer = server;
        this.salt = salt;
        this.randomSource = RandomSource.create(salt);
        this.currentModifiers = currentModifiers;
        this.currentModifierSlots = currentModifiers.size();
        this.currentTime = retrieveCurrentTime(server);
        this.disabled = false;
    }

    public RandomSource getRandomSource() {
        return randomSource;
    }

    public List<WorldModifier> getCurrentModifiers() {
        Excraft.LOGGER.info("Modifiers list " + currentModifiers);
        return currentModifiers;
    }
    public int getSalt() {
        return this.salt;
    }
/*
    For @currentModifierSlots total of modifiers, selects a random number and removes from the list a modifier with that index,
    adding it to the final list. hen it checks its weight. If the weight is below one, it rolls a dice to check if it
    will add it or not, removing it from the list and making it so it will continue rolling for more modifiers if not.
    If the modifier selected is a worldgenmodifier, it rolls a dice to check if it should add it as a bonus instead.

    Gets the impact of that modifier and adds it to the total impact.
    Then, on a while loop, if the impact of the modifiers is way too high compared to the current
    amount of slots OR a minimum tolerance of 5, it tries to fix it by first:

    Gets, at random, a modifier from the filtered list. If the modifier's removal would worsen the difference, it takes it out,
    and adds it to a safekeeping list, returning the while loop back to its start by doing continue. Otherwise, it instead removes
    its impact from the pool, and removes it entirely from the list, preventing it from being rolled again for this roll.
    it then tries to find the modifier that has a weight that, if added to the list, would either make its total impact 0,
    or the closest it can to 0. Furthermore, if the impact of the current modifier would result in the same net neutral impact,
    it then gives the modifier a 30% chance of replacing the old modifier with the new one. Then the process repeats.

    At the end, it removes all the WorldGenModifiers from the list and separates them to a different list.

    Todo: Add support for restrictions on modifiers and the ability for modifiers to impact the weight of other modifiers.
*/
    public List<WorldModifier> rollModifiers() {
        List<WorldModifier> unfilteredList = getWorldModifierList();
        List<WorldModifier> list = new ArrayList<>();
        Excraft.LOGGER.info("Rolling this many modifiers: " + currentModifierSlots);
        boolean wasSpecialModifierRolled = false;
        int currentImpactSum = 0;
        for (int i = 0; i < currentModifierSlots && !unfilteredList.isEmpty(); i++) {
            int modifierAtRandom = randomSource.nextIntBetweenInclusive(0,unfilteredList.size() - 1);
            WorldModifier selectedModifier = unfilteredList.remove(modifierAtRandom);
            if (selectedModifier.getWeight() < 1 && (selectedModifier.getWeight() >= randomSource.nextInt(100)/100)) {i--; continue;}
            if (selectedModifier instanceof SpecialWorldGenModifier && !wasSpecialModifierRolled) {
                wasSpecialModifierRolled = true;
            } else if (selectedModifier instanceof SpecialWorldGenModifier) {
                i--;
                list.remove(selectedModifier);
                continue;
            }
            list.add(selectedModifier);
            if (selectedModifier.getImpact() == 0 && randomSource.nextBoolean()) {i--;}
            Excraft.LOGGER.info("Current impact sum " + currentImpactSum + " adding " + selectedModifier.getImpact() + " total " + (currentImpactSum + selectedModifier.getImpact() + " of index " + modifierAtRandom ));
            currentImpactSum += selectedModifier.getImpact();
        }

        List<WorldModifier> safeKeeping = new ArrayList<>();
        int impactSumTolerance = Math.max(5,currentModifierSlots);
        while ((currentImpactSum > impactSumTolerance || currentImpactSum < -impactSumTolerance) && !unfilteredList.isEmpty()) {
            int modifierAtRandom = randomSource.nextIntBetweenInclusive(0,list.size() - 1);
            WorldModifier gottenModifier = list.get(modifierAtRandom);
            Excraft.LOGGER.info("bool check " + gottenModifier.getImpact() + currentImpactSum);
            if ((gottenModifier.getImpact() > 0 && currentImpactSum < 0) || (gottenModifier.getImpact() < 0 && currentImpactSum > 0)) {
                safeKeeping.add(list.remove(modifierAtRandom));
                continue;
            }
            Excraft.LOGGER.info("Resolving net impact by removing " + gottenModifier.getModifierName() + " with impact " + gottenModifier.getImpact() + " subtracting it with currentimpactsum of " + currentImpactSum + " of index " + modifierAtRandom);
            currentImpactSum = currentImpactSum - gottenModifier.getImpact();
            Excraft.LOGGER.info("Result of that was " + currentImpactSum);
            list.remove(modifierAtRandom);
            int findClosestValue = Math.abs(currentImpactSum);
            int currentIndex = 0;
            int indexOfFindClosestValue = 0;
            for (WorldModifier worldModifier:unfilteredList) {
                if (Math.abs(worldModifier.getImpact() + currentImpactSum) < findClosestValue) {
                    findClosestValue = Math.abs(worldModifier.getImpact() + currentImpactSum);
                    indexOfFindClosestValue = currentIndex;
                } else if (Math.abs(worldModifier.getImpact() + currentImpactSum) == findClosestValue && randomSource.nextIntBetweenInclusive(0,10) > 7) {
                    indexOfFindClosestValue = currentIndex;
                }
                currentIndex++;
            }
            WorldModifier replacementModifier = unfilteredList.remove(indexOfFindClosestValue);
            Excraft.LOGGER.info("Got replacement modifier " + replacementModifier + " of impact " + replacementModifier.getImpact() + " to add to " + currentImpactSum);
            list.add(replacementModifier);
            currentImpactSum = currentImpactSum + replacementModifier.getImpact();
        }

        list.addAll(safeKeeping);
        Excraft.LOGGER.info("Rolled " + list + " with a total net impact of " + currentImpactSum);
        List<WorldModifier> finalList = new ArrayList<>();
        int worldGenModifierIndex = 0;
        for (WorldModifier currentWorldModifier: list) {
            if (currentWorldModifier instanceof WorldGenWorldModifier) {
                Excraft.LOGGER.info("Thumbs up" + currentWorldModifier + " " + worldGenModifierIndex);
                worldGenModifiers.add((WorldGenWorldModifier) currentWorldModifier);
            } finalList.add(currentWorldModifier);
            worldGenModifierIndex++;
        }
        return finalList;
    }

    private long retrieveCurrentTime(MinecraftServer server) {
        if (server == null) {return 0;}
        else return currentTime;
    }

    public void worldModifierScheduler() {
        if (!minecraftServer.overworld().isClientSide()) {
            for (WorldModifier worldModifier : currentModifiers) {
                if (disabled) {
                    worldModifier.disabledEffect(minecraftServer, ExcraftDimensionManager.EXCRAFT_LEVEL);
                    Excraft.LOGGER.info(worldModifier + " got disabled");
                } else {
                    worldModifier.activateEffect(minecraftServer, ExcraftDimensionManager.EXCRAFT_LEVEL);
                }
            }
        }
    }
    public void disableModifierEffects() {
        disabled = true;
    }

    private List<WorldModifier> getWorldModifierList() {
        RegistryAccess registryAccess = minecraftServer.registryAccess();
        Registry<WorldModifier> worldModifiers = registryAccess.registryOrThrow(WorldModifierRegister.WORLD_MODIFIER_REGISTRY_KEY);
        List<WorldModifier> list = new ArrayList<>();
        for (Map.Entry<ResourceKey<WorldModifier>, WorldModifier> entry : worldModifiers.entrySet()) {
            if (Config.MODIFIERBLACKLIST.get().contains(entry.getValue().getModifierResourceLocation().toString())) {Excraft.LOGGER.info("aaa" + Config.MODIFIERBLACKLIST.get() + entry.getValue().getModifierResourceLocation().toString());continue;}
            list.addLast(entry.getValue());
        }
        return list;
    }

    public List<WorldGenWorldModifier> getWorldGenModifiers() {
        return worldGenModifiers;
    }
}

