package cloud.ejaonline.mc;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import cloud.ejaonline.mc.events.EventsHandler;

public class SitDown extends JavaPlugin {
    private FileConfiguration config;
    private JavaPlugin plugin;

    @Override
    public void onEnable() {
        // Variables
        plugin = this;
        saveDefaultConfig();
        config = getConfig();

        // Commands and Permissions
        Helper helper = new Helper(plugin, config);
        new DataInitializer(plugin, config, helper);

        // Events
        getServer().getPluginManager().registerEvents(new EventsHandler(config, helper), this);
    }

    @Override
    public void onDisable() {
        // Disable
    }
}