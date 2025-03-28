package ru.abstractmenus.data.actions.wrappers;

import ru.abstractmenus.data.Actions;
import ru.abstractmenus.hocon.api.ConfigNode;
import ru.abstractmenus.hocon.api.serialize.NodeSerializeException;
import ru.abstractmenus.hocon.api.serialize.NodeSerializer;
import org.bukkit.entity.Player;
import ru.abstractmenus.api.Action;
import ru.abstractmenus.api.inventory.Item;
import ru.abstractmenus.api.inventory.Menu;

import java.util.List;

public class ActionBulk implements Action {

    private final List<Actions> actions;

    private ActionBulk(List<Actions> actions) {
        this.actions = actions;
    }

    @Override
    public void activate(Player player, Menu menu, Item clickedItem) {
        for (Actions action : actions) {
            action.activate(player, menu, clickedItem);
        }
    }

    public static class Serializer implements NodeSerializer<ActionBulk> {

        @Override
        public ActionBulk deserialize(Class type, ConfigNode node) throws NodeSerializeException {
            return new ActionBulk(node.getList(Actions.class));
        }

    }
}
