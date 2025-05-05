package ru.abstractmenus.data.properties;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.abstractmenus.api.inventory.ItemProperty;
import ru.abstractmenus.api.inventory.Menu;
import ru.abstractmenus.api.text.Colors;
import ru.abstractmenus.hocon.api.ConfigNode;
import ru.abstractmenus.hocon.api.serialize.NodeSerializeException;
import ru.abstractmenus.hocon.api.serialize.NodeSerializer;
import ru.abstractmenus.util.adventure.AdventureUtil;

import java.util.ArrayList;
import java.util.List;

public class PropLore implements ItemProperty {

    private final List<String> lore;

    private PropLore(List<String> lore){
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
        TagResolver[] tagResolvers = new TagResolver[] {
                AdventureUtil.papiTagResolver(player, true),
        };

        List<Component> loreComponents = new ArrayList<>();
        for (String loreLine : lore) {
            Component component = AdventureUtil.parseMiniMessage(loreLine, tagResolvers);
            loreComponents.add(component);
        }

        meta.lore(loreComponents);
    }

    public static class Serializer implements NodeSerializer<PropLore> {

        @Override
        public PropLore deserialize(Class type, ConfigNode node) throws NodeSerializeException {
            return new PropLore(Colors.ofList(node.getList(String.class)));
        }

    }
}
