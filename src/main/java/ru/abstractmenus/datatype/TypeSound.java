package ru.abstractmenus.datatype;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import ru.abstractmenus.api.inventory.Menu;
import ru.abstractmenus.hocon.api.ConfigNode;
import ru.abstractmenus.hocon.api.serialize.NodeSerializeException;
import ru.abstractmenus.hocon.api.serialize.NodeSerializer;

public class TypeSound extends DataType {

    public TypeSound(String soundKey) {
        super(soundKey);
    }

    public Sound getSound(Player player, Menu menu) {
        String resolvedKey = replaceFor(player, menu);
        return Registry.SOUNDS.get(NamespacedKey.fromString(resolvedKey));
    }

    public static class Serializer implements NodeSerializer<TypeSound> {

        @Override
        public TypeSound deserialize(Class type, ConfigNode node) throws NodeSerializeException {
            String soundKey = node.getString();
            if (Registry.SOUNDS.get(NamespacedKey.fromString(soundKey)) == null) {
                throw new NodeSerializeException(node, "Invalid sound name: " + soundKey);
            }
            return new TypeSound(soundKey);
        }
    }
}
