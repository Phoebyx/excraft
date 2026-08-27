package com.example.excraft.dimension.worldmodifiers.events;

import com.example.excraft.data.ExcraftTimer;
import com.example.excraft.dimension.worldmodifiers.WorldModifierType;
import net.minecraft.server.MinecraftServer;

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
