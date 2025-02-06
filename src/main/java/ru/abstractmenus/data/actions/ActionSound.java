package ru.abstractmenus.data.actions;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import ru.abstractmenus.datatype.*;
import ru.abstractmenus.hocon.api.ConfigNode;
import ru.abstractmenus.hocon.api.serialize.NodeSerializeException;
import ru.abstractmenus.hocon.api.serialize.NodeSerializer;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import ru.abstractmenus.api.Action;
import ru.abstractmenus.api.inventory.Menu;
import ru.abstractmenus.api.inventory.Item;
import ru.abstractmenus.util.StringUtil;

public class ActionSound implements Action {

    private final TypeSound sound;
    private final TypeFloat volume, pitch;
    private final TypeBool isPublic;
    private final TypeLocation location;

    private ActionSound(TypeSound sound, TypeFloat volume, TypeFloat pitch, TypeBool isPublic, TypeLocation location) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
        this.isPublic = isPublic;
        this.location = location;
    }

    @Override
    public void activate(Player player, Menu menu, Item clickedItem) {
        if (sound != null) {
            Location loc = (location != null) ? location.getLocation(player, menu) : player.getLocation();
            Sound soundObj = sound.getSound(player, menu);
            if (soundObj == null) return;

            if (isPublic.getBool(player, menu)) {
                loc.getWorld().playSound(loc, soundObj, volume.getFloat(player, menu), pitch.getFloat(player, menu));
            } else {
                player.playSound(loc, soundObj, volume.getFloat(player, menu), pitch.getFloat(player, menu));
            }
        }
    }

    public static class Serializer implements NodeSerializer<ActionSound> {

        @Override
        public ActionSound deserialize(Class type, ConfigNode node) throws NodeSerializeException {
            if (node.isPrimitive()) {
                String name = node.getString("null");
                if (!StringUtil.contains(name, '%') && Registry.SOUNDS.get(NamespacedKey.fromString(name)) == null) {
                    throw new NodeSerializeException(node, "Cannot read sound action with sound name '" + name + "'. Invalid sound name.");
                }
                return new ActionSound(new TypeSound(name), new TypeFloat(1.0f), new TypeFloat(1.0f), new TypeBool(false), null);
            }

            if (node.isMap()) {
                String name = node.node("name").getString(node.getString());
                TypeSound sound;
                TypeFloat volume = new TypeFloat(1.0f);
                TypeFloat pitch = new TypeFloat(1.0f);
                TypeBool isPublic = new TypeBool(false);
                TypeLocation location = null;

                if (!StringUtil.contains(name, '%') && Registry.SOUNDS.get(NamespacedKey.fromString(name)) == null) {
                    throw new NodeSerializeException(node, "Cannot read sound action with sound name '" + name + "'. Invalid sound name.");
                }
                sound = new TypeSound(name);

                if (node.node("volume").rawValue() != null) {
                    volume = node.node("volume").getValue(TypeFloat.class);
                }

                if (node.node("pitch").rawValue() != null) {
                    pitch = node.node("pitch").getValue(TypeFloat.class);
                }

                if (node.node("public").rawValue() != null) {
                    isPublic = node.node("public").getValue(TypeBool.class);
                }

                if (node.node("location").rawValue() != null) {
                    location = node.node("location").getValue(TypeLocation.class);
                }

                return new ActionSound(sound, volume, pitch, isPublic, location);
            }

            throw new NodeSerializeException(node, "Cannot read sound action. Invalid format");
        }
    }
}