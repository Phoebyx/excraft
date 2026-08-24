package com.example.excraft.inventory;

import com.example.excraft.Excraft;
import com.example.excraft.dimension.ExcraftDimensionManager;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.component.Unbreakable;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;
import net.neoforged.neoforge.event.entity.EntityTravelToDimensionEvent;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.UUID;
import java.util.logging.Logger;

public class DurabilityChanges {
    private static UUID entityUUID;
    @SubscribeEvent
    public static void recordWhoLeft(EntityLeaveLevelEvent event) {
        if (entityCheck(event)) {
            entityUUID = event.getEntity().getUUID();
            Excraft.LOGGER.info(String.valueOf(entityUUID));
        }
    }
    @SubscribeEvent
    public static void damageByPortalReturn(EntityJoinLevelEvent event) {
        if (event.getEntity().getUUID().equals(entityUUID) && event.getLevel().dimension() == Level.OVERWORLD) {
            Excraft.LOGGER.info("Checking Inventory of " + event.getEntity() + " Event Level " + event.getLevel().dimension() + " Entity Level " + event.getEntity().level().dimension());
            IItemHandler entityCapability = Capabilities.ItemHandler.ENTITY_AUTOMATION.getCapability(event.getEntity(),null);
            Excraft.LOGGER.info("entity capability " + entityCapability);
            if (entityCapability == null) {
                entityCapability = Capabilities.ItemHandler.ENTITY.getCapability(event.getEntity(),null);
            }
            if (entityCapability != null) {
                doEntityDamage(entityCapability);
            }
        }
    }

    private static boolean entityCheck(EntityLeaveLevelEvent event) {
        return event.getLevel().dimension() != Level.OVERWORLD && event.getEntity().getRemovalReason() == Entity.RemovalReason.CHANGED_DIMENSION;
    }

    private static void damageEquipment(ItemStack currentItemStack,IItemHandler currentCapability,int i) {
        int itemStackMaxDamage = currentItemStack.getMaxDamage();
        int setDamageTo = currentItemStack.getDamageValue() + Math.toIntExact(itemStackMaxDamage / Math.round(Math.sqrt((double) itemStackMaxDamage * 0.1)));
        ItemStack removedItemToModify = currentCapability.extractItem(i,currentItemStack.getCount(),false);
        removedItemToModify.getItem().setDamage(removedItemToModify, setDamageTo);
        currentCapability.insertItem(i,removedItemToModify,false);
        Excraft.LOGGER.info("damaged " + removedItemToModify + " by " + setDamageTo);
    }

    private static void doEntityDamage(IItemHandler currentCapability) {
        if (currentCapability != null) {
            for (int i = 0; i < currentCapability.getSlots(); i++) {
                ItemStack currentItemStack = currentCapability.getStackInSlot(i);
                Excraft.LOGGER.info("testing slot " + i + " found " + currentItemStack + " has damage of " + currentItemStack.getDamageValue());
                IItemHandler itemCapability = Capabilities.ItemHandler.ITEM.getCapability(currentItemStack,null);
                if (itemCapability != null) {
                    Excraft.LOGGER.info("Found item with inventory, checking " + currentItemStack);
                    doEntityDamage(itemCapability);
                }
                if (currentItemStack.getMaxDamage() != 0) {
                    damageEquipment(currentItemStack,currentCapability,i);
                }
            }
        }
    }
}
