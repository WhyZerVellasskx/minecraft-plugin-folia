package ru.abstractmenus.placeholders.hooks;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import ru.abstractmenus.placeholders.PlaceholderHook;

import java.util.Arrays;

public class ServerPlaceholders implements PlaceholderHook {

    @Override
    public String replace(String placeholder, Player player) {
        if (placeholder.startsWith("players_")) {
            String[] arr = placeholder.split("_");
            String worldName = String.join("_", Arrays.copyOfRange(arr, 2, arr.length));
            World world = Bukkit.getWorld(worldName);
            return world != null ? String.valueOf(world.getPlayers().size()) : "0";
        }

        return switch (placeholder) {
            case "name" -> Bukkit.getServer().getName();
            case "ip" -> Bukkit.getServer().getIp();
            case "port" -> String.valueOf(Bukkit.getServer().getPort());
            case "players" -> String.valueOf(Bukkit.getOnlinePlayers().size());
            case "max_players" -> String.valueOf(Bukkit.getMaxPlayers());
            case "version" -> Bukkit.getVersion();
            default -> null;
        };
    }

}
