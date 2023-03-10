package beeted.events;

import me.clip.placeholderapi.PlaceholderAPI;
import beeted.customwelcome.CustomWelcome;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class PlayerJoinQuit implements Listener {
    private final CustomWelcome plugin;

    public PlayerJoinQuit(CustomWelcome plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void Join(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = plugin.getConfig();
        String path = "config.enable-join-message";

        if (config.getString(path).equals("true")) {
            String text = "config.join-message";
            String message = config.getString(text);
            message = PlaceholderAPI.setPlaceholders(player, message);
            event.setJoinMessage(ChatColor.translateAlternateColorCodes('&', message));
        } else {
            event.setJoinMessage(null);
        }

        String motdpath = "motd.enable-motd-message";

        if (config.getString(motdpath).equals("true")) {
            String text = "motd.motd-message";
            List<String> messages = plugin.getConfig().getStringList(text);

            for (int i=0; i<messages.size();i++) {
                String motdtext = PlaceholderAPI.setPlaceholders(player, messages.get(i));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', motdtext));
            }
        } else {
            player.sendMessage((String) null);
        }
    }

    @EventHandler
    public void Quit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = plugin.getConfig();
        String path = "config.enable-quit-message";

        if (config.getString(path).equals("true")) {
            String text = "config.quit-message";
            String message = config.getString(text);
            message = PlaceholderAPI.setPlaceholders(player, message);
            event.setQuitMessage(ChatColor.translateAlternateColorCodes('&', message));
        } else {
            event.setQuitMessage(null);
        }
    }
}
