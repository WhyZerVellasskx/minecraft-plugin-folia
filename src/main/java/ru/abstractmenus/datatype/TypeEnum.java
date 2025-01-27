package ru.abstractmenus.datatype;

import org.bukkit.Sound;
import ru.abstractmenus.hocon.api.ConfigNode;
import ru.abstractmenus.hocon.api.serialize.NodeSerializeException;
import ru.abstractmenus.hocon.api.serialize.NodeSerializer;
import org.bukkit.entity.Player;
import ru.abstractmenus.api.inventory.Menu;

import java.util.Locale;

public class TypeEnum<E> extends DataType {

    private E value = null;

    public TypeEnum(String value) {
        super(value);
    }

    public TypeEnum(E value) {
        super(null);
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    public E getEnum(Class<E> type, Player player, Menu menu) throws IllegalArgumentException {
        if (value != null) {
            return value;
        }

        String key = replaceFor(player, menu).toLowerCase(Locale.ROOT);
        if (type == Sound.class) {
            return (E) Sound.valueOf(key);
        }

        throw new IllegalArgumentException("Unsupported enum type: " + type.getName());
    }

    public static class Serializer implements NodeSerializer<TypeEnum> {

        @Override
        public TypeEnum deserialize(Class type, ConfigNode node) throws NodeSerializeException {
            return new TypeEnum(node.getString());
        }
    }
}
