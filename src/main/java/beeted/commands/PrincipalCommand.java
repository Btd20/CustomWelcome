package beeted.commands;

import beeted.customwelcome.CustomWelcome;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class PrincipalCommand implements CommandExecutor {
    private final String name = ChatColor.GRAY+"["+ChatColor.AQUA+"CustomWelcome"+ChatColor.GRAY+"] ";
    private final CustomWelcome plugin;
    public PrincipalCommand (CustomWelcome plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(name+ChatColor.RED+"You can't execute commands from console.");
            return false;
        } else {
            Player player = (Player) sender;
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("version")) {
                    player.sendMessage(name + ChatColor.DARK_GREEN + "Plugin version: 1.0");
                    return true;
                } else if (args[0].equalsIgnoreCase("reload")) {
                    plugin.reloadConfig();
                    player.sendMessage(name + ChatColor.DARK_GREEN + "Plugin reloaded.");
                    return true;
                } else if (args[0].equalsIgnoreCase("motd")) {
                    String text = "motd.motd-message";
                    List<String> messages = plugin.getConfig().getStringList(text);
                    for (int i=0; i<messages.size();i++) {
                        String motdtext = PlaceholderAPI.setPlaceholders(player, messages.get(i));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', motdtext));
                    }
                    return true;
                } else {
                    player.sendMessage(name+ChatColor.DARK_RED+"This command does not exist.");
                    return true;
                }
            } else {
                player.sendMessage(name+ChatColor.GRAY+"This command does not exist.\n"+ChatColor.GRAY+"Valid commands are:\n"+ChatColor.RED+"/cw version"+ChatColor.DARK_GRAY+" - Check the plugin version.\n"+ChatColor.RED+"/cw reload"+ChatColor.DARK_GRAY+" - Reload the plugin config.");
                return true;
            }
        }
    }
}
