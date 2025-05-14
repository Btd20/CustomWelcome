package beeted.customwelcome;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class UpdateIcon {

    private final JavaPlugin plugin;
    private final String ICON_FOLDER;
    private static final String SERVER_ICON_PATH = "server-icon.png";

    public UpdateIcon(JavaPlugin plugin) {
        this.plugin = plugin;
        this.ICON_FOLDER = plugin.getDataFolder() + File.separator + "server-icon";
    }

    /*public void startWatching() {
        // Crear la carpeta server-icon si no existe
        createIconFolder();

        // Iniciar la tarea asíncrona de observación de cambios en el directorio
        Bukkit.getScheduler().runTaskAsynchronously(plugin, this::watchForIconUpdate);
    }*/

    public void createIconFolder() {
        File iconFolder = new File(ICON_FOLDER);
        if (!iconFolder.exists()) {
            if (iconFolder.mkdirs()) {
                Bukkit.getLogger().info("'Server-icon' folder created successfully in " + ICON_FOLDER);
            } else {
                Bukkit.getLogger().severe("Could not create folder 'server-icon'.");
            }
        }
    }

    /*private void watchForIconUpdate() {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path path = Paths.get(ICON_FOLDER);
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);

            while (true) {
                WatchKey key = watchService.take();

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    if (kind == StandardWatchEventKinds.ENTRY_CREATE || kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                        File newIcon = new File(ICON_FOLDER, event.context().toString());

                        if (newIcon.getName().endsWith(".png")) {
                            updateServerIcon(newIcon);
                        }
                    }
                }
                key.reset();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }*/

    private void updateServerIcon(File newIcon) {
        File serverIcon = new File(SERVER_ICON_PATH);

        try {
            if (newIcon.exists() && newIcon.isFile()) {
                Files.copy(newIcon.toPath(), serverIcon.toPath(), StandardCopyOption.REPLACE_EXISTING);
                Bukkit.getLogger().info("The server icon has been successfully updated.");
            } else {
                // Elimina el icono si no hay una imagen válida
                if (serverIcon.exists()) {
                    serverIcon.delete();
                    Bukkit.getLogger().info("No valid icon found. The icon has been removed from the server.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Elimina el icono en caso de error
            if (serverIcon.exists()) {
                serverIcon.delete();
                Bukkit.getLogger().info("An error occurred. The server icon was removed.");
            }
        }
    }
}
