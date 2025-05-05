package ru.abstractmenus.data.properties;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.abstractmenus.api.inventory.Menu;
import ru.abstractmenus.api.inventory.ItemProperty;
import ru.abstractmenus.util.adventure.AdventureUtil;
import ru.abstractmenus.hocon.api.ConfigNode;
import ru.abstractmenus.hocon.api.serialize.NodeSerializeException;
import ru.abstractmenus.hocon.api.serialize.NodeSerializer;

import java.util.List;

public class PropLore implements ItemProperty {

    private final List<String> lore;

    private PropLore(List<String> lore) {
        this.lore = lore;
    }

    @Override
    public boolean canReplaceMaterial() {
        return false;
    }

    @Override
    public boolean isApplyMeta() {
        return true;
    }

    @Override
    public void apply(ItemStack itemStack, ItemMeta meta, Player player, Menu menu) {
        List<Component> formattedLore = lore.stream()
                .map(loreLine -> AdventureUtil.parseMiniMessage(
                        loreLine,
                        AdventureUtil.papiTagResolver(player, true)
                ))
                .toList();

        meta.lore(formattedLore);
    }

    public static class Serializer implements NodeSerializer<PropLore> {

        @Override
        public PropLore deserialize(Class type, ConfigNode node) throws NodeSerializeException {
            return new PropLore(node.getList(String.class));
        }
    }
}
