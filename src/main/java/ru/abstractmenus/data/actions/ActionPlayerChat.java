package ru.abstractmenus.data.actions;


import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import ru.abstractmenus.api.Action;
import ru.abstractmenus.api.inventory.Item;
import ru.abstractmenus.api.inventory.Menu;
import ru.abstractmenus.hocon.api.ConfigNode;
import ru.abstractmenus.hocon.api.serialize.NodeSerializeException;
import ru.abstractmenus.hocon.api.serialize.NodeSerializer;
import ru.abstractmenus.util.adventure.AdventureUtil;

import java.util.List;

public class ActionPlayerChat implements Action {

    private final List<String> messages;

    private ActionPlayerChat(List<String> messages) {
        this.messages = messages;
    }

    @Override
    public void activate(Player player, Menu menu, Item clickedItem) {
        messages.forEach(message -> {
            Component formattedMessage = AdventureUtil.parseMiniMessage(message);
            player.sendMessage(formattedMessage);
        });
    }

    public static class Serializer implements NodeSerializer<ActionPlayerChat> {

        @Override
        public ActionPlayerChat deserialize(Class type, ConfigNode node) throws NodeSerializeException {
            return new ActionPlayerChat(node.getList(String.class));
        }
    }
}
