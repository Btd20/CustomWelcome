package beeted.commands;

import beeted.customwelcome.CustomWelcome;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
    private final String name = ChatColor.GRAY+"["+ChatColor.AQUA+"CustomWelcome"+ChatColor.GRAY+"] ";
    public CustomWelcome plugin;
    public Commands (CustomWelcome plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(name+ChatColor.RED+"You can't execute commands from console.");
            return false;
        } else {
            Player player = (Player) sender;
            if (args.length > 0) {
                return true;
            } else {
                player.sendMessage(name + ChatColor.DARK_RED + "This command does not exist.");
                return true;
            }
        }
    }
}
