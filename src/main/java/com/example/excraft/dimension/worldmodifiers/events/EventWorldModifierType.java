package com.example.excraft.dimension.worldmodifiers.events;

import com.example.excraft.Excraft;
import com.example.excraft.data.ExcraftTimer;
import com.example.excraft.dimension.DimensionRandomizer;
import com.example.excraft.dimension.ExcraftDimensionManager;
import com.example.excraft.dimension.WorldModifierManager;
import com.example.excraft.dimension.worldmodifiers.WorldModifier;
import com.example.excraft.dimension.worldmodifiers.WorldModifierType;
import net.commoble.infiniverse.internal.DimensionManager;
import net.minecraft.server.MinecraftServer;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public abstract class EventWorldModifierType implements WorldModifierType {
    private String modifierType = "Event";
    private Boolean cycle;
    private long timeInSeconds;

    public EventWorldModifierType() {
    }

    @Override
    public String getModifierName() {
        return this.modifierType;
    }
    public void setCurrentTime(MinecraftServer server){
        this.timeInSeconds = ExcraftTimer.getCurrentTimer(server);
    }

    public long getTimeInSecondsToEvents() {
        return this.timeInSeconds;
    }

    public Boolean getCycle() {
        return cycle;
    }
}
