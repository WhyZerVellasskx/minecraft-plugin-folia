package ru.abstractmenus.data.activators;

import lombok.val;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import ru.abstractmenus.api.ValueExtractor;
import ru.abstractmenus.hocon.api.ConfigNode;
import ru.abstractmenus.hocon.api.serialize.NodeSerializeException;
import ru.abstractmenus.hocon.api.serialize.NodeSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import ru.abstractmenus.api.inventory.Item;
import ru.abstractmenus.api.Activator;
import ru.abstractmenus.api.Logger;
import ru.abstractmenus.extractors.ItemStackExtractor;

import java.util.List;

public class OpenClickItem extends Activator {

    private final List<Item> items;

    private OpenClickItem(List<Item> items) {
        this.items = items;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (items == null) return;

        if (event.getItem() == null) return;
        if (event.getHand() != EquipmentSlot.HAND) return;

        if (event.getAction().isRightClick()) {
            for (Item item : items) {
                try {
                    if (item.isSimilar(event.getItem(), event.getPlayer())) {
                        event.setCancelled(true);
                        openMenu(event.getItem(), event.getPlayer());
                        return;
                    }
                } catch (Exception e) {
                    Logger.severe("Cannot execute clickItem activator: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public ValueExtractor getValueExtractor() {
        return ItemStackExtractor.INSTANCE;
    }

    public static class Serializer implements NodeSerializer<OpenClickItem> {

        @Override
        public OpenClickItem deserialize(Class type, ConfigNode node) throws NodeSerializeException {
            return new OpenClickItem(node.getList(Item.class));
        }

    }
}
