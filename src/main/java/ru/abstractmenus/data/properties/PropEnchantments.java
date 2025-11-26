package ru.abstractmenus.data.properties;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.abstractmenus.api.inventory.ItemProperty;
import ru.abstractmenus.api.inventory.Menu;
import ru.abstractmenus.datatype.TypeInt;
import ru.abstractmenus.hocon.api.ConfigNode;
import ru.abstractmenus.hocon.api.serialize.NodeSerializeException;
import ru.abstractmenus.hocon.api.serialize.NodeSerializer;

import java.util.HashMap;
import java.util.Map;

public record PropEnchantments(Map<Enchantment, TypeInt> enchantments)
        implements ItemProperty {

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
        for (Map.Entry<Enchantment, TypeInt> entry : enchantments.entrySet()) {
            meta.addEnchant(entry.getKey(), entry.getValue().getInt(player, menu), true);
        }
    }

    public static class Serializer implements NodeSerializer<PropEnchantments> {

        @Override
        public PropEnchantments deserialize(Class type, ConfigNode node) throws NodeSerializeException {
            Map<String, ConfigNode> nodes = node.childrenMap();
            Map<Enchantment, TypeInt> map = new HashMap<>();

            RegistryAccess registryAccess = RegistryAccess.registryAccess();
            var enchantRegistry = registryAccess.getRegistry(RegistryKey.ENCHANTMENT);

            for (Map.Entry<String, ConfigNode> entry : nodes.entrySet()) {
                String key = entry.getKey();
                if (!key.contains(":")) {
                    key = "minecraft:" + key.toLowerCase();
                }

                NamespacedKey namespacedKey = NamespacedKey.fromString(key);
                if (namespacedKey == null) continue;

                Enchantment enchantment = enchantRegistry.get(namespacedKey);
                if (enchantment != null) {
                    map.put(enchantment, entry.getValue().getValue(TypeInt.class));
                }
            }

            return new PropEnchantments(map);
        }
    }
}