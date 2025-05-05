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

public class PropName implements ItemProperty {

    private final String name;

    private PropName(String name){
        this.name = name;
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
        Component formatted = AdventureUtil.parseMiniMessage(name, tagResolvers);

        meta.displayName(formatted);
    }

    public static class Serializer implements NodeSerializer<PropName> {

        @Override
        public PropName deserialize(Class type, ConfigNode node) throws NodeSerializeException {
            return new PropName(Colors.of(node.getString()));
        }

    }
}
