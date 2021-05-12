package cloud.ejaonline.mc;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import cloud.ejaonline.mc.events.EventsHandler;

public class SitDown extends JavaPlugin {

    public FileConfiguration config;
    public JavaPlugin plugin;
    public static Permission sitPermission;
    public static Permission adminPermission;
    public static Permission wildcardPermission;
    public static boolean pluginEnabled;

    @Override
    public void onEnable() {
        // Variables
        plugin = this;
        saveDefaultConfig();
        config = getConfig();
        pluginEnabled = config.getBoolean("enabled");

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