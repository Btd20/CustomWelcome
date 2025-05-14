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
import org.bukkit.event.server.ServerListPingEvent;

import java.util.ArrayList;
import java.util.Comparator;
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

        if (config.getBoolean("config.clear-chat-on-join")) {
            for (int i = 0; i < 100; i++) {
                player.sendMessage("");
            }
        }

        List<JoinComponent> components = new ArrayList<>();

        // Mensaje de bienvenida o primer ingreso
        if (!player.hasPlayedBefore() && config.getBoolean("config.enable-first-join-message")) {
            String rawMessage = config.getString("config.first-join-message", "");
            int count = config.getInt("player-count");
            config.set("player-count", count + 1);
            plugin.saveConfig();

            rawMessage = rawMessage.replace("%player_count%", String.valueOf(count));
            String finalMessage = PlaceholderAPI.setPlaceholders(player, rawMessage);
            components.add(new JoinComponent(config.getInt("config.priority"), ChatColor.translateAlternateColorCodes('&', finalMessage)));
            event.setJoinMessage(null); // Evitamos que Bukkit envíe un mensaje automático
        } else if (player.hasPlayedBefore() && config.getBoolean("config.enable-join-message")) {
            String rawMessage = config.getString("config.join-message", "");
            String finalMessage = PlaceholderAPI.setPlaceholders(player, rawMessage);
            components.add(new JoinComponent(config.getInt("config.priority"), ChatColor.translateAlternateColorCodes('&', finalMessage)));
            event.setJoinMessage(null);
        } else {
            event.setJoinMessage(null);
        }

        // MOTD
        if (config.getBoolean("motd.enable-motd-message")) {
            List<String> motdLines = config.getStringList("motd.motd-message");
            int priority = config.getInt("motd.priority");

            StringBuilder motdBuilder = new StringBuilder();
            for (String line : motdLines) {
                line = PlaceholderAPI.setPlaceholders(player, line);
                motdBuilder.append(ChatColor.translateAlternateColorCodes('&', line)).append("\n");
            }

            components.add(new JoinComponent(priority, motdBuilder.toString()));
        }

        // Ordenar por prioridad (menor valor = más prioritario)
        components.sort(Comparator.comparingInt(c -> c.priority));

        // Enviar todos los mensajes ordenados
        for (JoinComponent comp : components) {
            for (String line : comp.message.split("\n")) {
                player.sendMessage(line);
            }
        }
    }

    // Clase interna simple para manejar prioridad y mensaje
    private static class JoinComponent {
        int priority;
        String message;

        public JoinComponent(int priority, String message) {
            this.priority = priority;
            this.message = message;
        }
    }

    // Método para enviar los mensajes del MOTD respetando la prioridad
    private void sendMotdMessages(Player player) {
        FileConfiguration config = plugin.getConfig();
        if (config.getBoolean("motd.enable-motd-message")) {
            List<String> motdMessages = config.getStringList("motd.motd-message");
            for (String motdText : motdMessages) {
                motdText = PlaceholderAPI.setPlaceholders(player, motdText);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', motdText));
            }
        }
    }

    // Método para enviar los mensajes de bienvenida
    private void sendWelcomeMessages(Player player) {
        FileConfiguration config = plugin.getConfig();
        if (config.getBoolean("config.enable-first-join-message")) {
            String message = config.getString("config.first-join-message");
            message = message.replace("%player_name%", player.getName())
                    .replace("%player_count%", String.valueOf(config.getInt("player-count")));
            message = PlaceholderAPI.setPlaceholders(player, message);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
        if (config.getBoolean("config.enable-join-message")) {
            String message = config.getString("config.join-message");
            message = message.replace("%player_name%", player.getName());
            message = PlaceholderAPI.setPlaceholders(player, message);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
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

    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        FileConfiguration config = plugin.getConfig();
        String path = "server-list-motd.enable-server-list-motd-message";

        if (config.getString(path).equals("true")) {
            String motd = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("server-list-motd.message"));
            event.setMotd(motd);
        }
    }
}
