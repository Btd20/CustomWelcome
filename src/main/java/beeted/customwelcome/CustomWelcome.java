package beeted.customwelcome;

import beeted.commands.Commands;
import beeted.commands.PrincipalCommand;
import beeted.events.PlayerJoinQuit;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class CustomWelcome extends JavaPlugin {
    ConsoleCommandSender console = Bukkit.getConsoleSender();
    public String configRute;
    @Override
    public void onEnable() {
        console.sendMessage("[CustomWelcome] Reading config.yml.");
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            /*
             * We register the EventListener here, when PlaceholderAPI is installed.
             * Since all events are in the main class (this class), we simply use "this"
             */
            console.sendMessage("[CustomWelcome] Found PlaceholderAPI installed.");
        } else {
            /*
             * We inform about the fact that PlaceholderAPI isn't installed and then
             * disable this plugin to prevent issues.
             */
            getLogger().warning("PlaceholderAPI not found. PlaceholderAPI is required.");
        }
        registerCommands();
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        registerEvents();
        registerConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        console.sendMessage("[CustomWelcome] Saving your messages...");
        console.sendMessage("[CustomWelcome] Saving config.");
    }

    public void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerJoinQuit(this), this);
    }
    public void registerConfig(){
        File config = new File(this.getDataFolder(),"config.yml");
        configRute = config.getPath();
        if(!config.exists()){
            this.getConfig().options().copyDefaults(true);
            saveDefaultConfig();
        }
    }

    public void registerCommands() {
        this.getCommand("version").setExecutor(new Commands(this));
        this.getCommand("cw").setExecutor(new PrincipalCommand(this));
    }
}
