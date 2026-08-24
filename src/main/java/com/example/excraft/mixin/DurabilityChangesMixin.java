package com.example.excraft.mixin;

import com.example.excraft.dimension.DimensionCreator;
import com.example.excraft.dimension.ExcraftDimensionManager;
import net.commoble.infiniverse.internal.DimensionManager;
import net.minecraft.advancements.critereon.ItemDurabilityTrigger;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityAttachments;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Tool;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.apache.logging.log4j.core.jmx.Server;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public class DurabilityChangesMixin {
    @Inject(method = "setDamageValue",at = @At(value = "HEAD"),cancellable = true)
    public void setDamageValue(int damage, CallbackInfo ci) {
        damage = 0;
        ci.cancel();
    }

}
