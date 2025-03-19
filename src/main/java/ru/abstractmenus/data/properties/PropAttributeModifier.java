package ru.abstractmenus.data.properties;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.abstractmenus.api.inventory.ItemProperty;
import ru.abstractmenus.api.inventory.Menu;
import ru.abstractmenus.hocon.api.ConfigNode;
import ru.abstractmenus.hocon.api.serialize.NodeSerializeException;
import ru.abstractmenus.hocon.api.serialize.NodeSerializer;

import java.util.HashMap;
import java.util.Map;

public class PropAttributeModifier implements ItemProperty {

    private final Map<Attribute, AttributeModifier> modifiers;

    private PropAttributeModifier(Map<Attribute, AttributeModifier> modifiers) {
        this.modifiers = modifiers;
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
        for (Map.Entry<Attribute, AttributeModifier> entry : modifiers.entrySet()) {
            meta.addAttributeModifier(entry.getKey(), entry.getValue());
        }
    }

    public static class Serializer implements NodeSerializer<PropAttributeModifier> {

        @Override
        @SuppressWarnings("UnstableApiUsage")
        public PropAttributeModifier deserialize(Class type, ConfigNode node) throws NodeSerializeException {
            Map<String, ConfigNode> nodes = node.childrenMap();
            Map<Attribute, AttributeModifier> map = new HashMap<>();

            for (Map.Entry<String, ConfigNode> entry : nodes.entrySet()) {
                try {
                    Attribute attribute = Registry.ATTRIBUTE.getOrThrow(NamespacedKey.minecraft(entry.getKey().toLowerCase()));
                    double amount = entry.getValue().node("amount").getDouble(0);
                    AttributeModifier.Operation operation = AttributeModifier.Operation.valueOf(entry.getValue().node("operation").getString("add_number").toUpperCase());

                    EquipmentSlot slot = null;
                    String slotName = entry.getValue().node("slot").getString(null);

                    if (slotName != null) {
                        try {
                            slot = EquipmentSlot.valueOf(slotName.toUpperCase());
                        } catch (IllegalArgumentException ignored) {
                            // ignored
                        }
                    }

                    NamespacedKey key = NamespacedKey.minecraft("custom_modifier_" + attribute.getKey().getKey());
                    AttributeModifier modifier;

                    if (slot != null) {
                        modifier = new AttributeModifier(key, amount, operation, slot.getGroup());
                    } else {
                        modifier = new AttributeModifier(key, amount, operation);
                    }

                    map.put(attribute, modifier);
                } catch (IllegalArgumentException e) {
                    throw new NodeSerializeException("Invalid attribute or operation: " + entry.getKey(), e);
                }
            }

            return new PropAttributeModifier(map);
        }
    }
}