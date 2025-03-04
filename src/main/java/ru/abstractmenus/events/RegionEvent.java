package ru.abstractmenus.events;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class RegionEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final ProtectedRegion region;
    private final Player player;

    public RegionEvent(ProtectedRegion region, Player player) {
        this.region = region;
        this.player = player;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
