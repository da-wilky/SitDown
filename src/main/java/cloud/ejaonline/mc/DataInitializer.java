package cloud.ejaonline.mc;

import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import cloud.ejaonline.mc.commands.AdminCommandHandler;
import cloud.ejaonline.mc.commands.CommandHandler;

public class DataInitializer {
    private JavaPlugin plugin;
    private FileConfiguration config;
    private Helper helper;

    public DataInitializer(JavaPlugin plugin, FileConfiguration config, Helper helper) {
        this.plugin = plugin;
        this.config = config;
        this.helper = helper;
        permissions();
        commands();
    }

    private void commands() {
        PluginCommand sit = plugin.getCommand("sit");
        sit.setExecutor(new CommandHandler(config, helper));
        sit.setDescription("Sit down.");
        sit.setUsage("Usage: /<command>");
        sit.setPermission(plugin.getDescription().getPermissions().get(0).getName());
        sit.setPermissionMessage(helper.transStr(config.getString("missing-permission-message")));

        PluginCommand admin = plugin.getCommand("sitdown");
        admin.setExecutor(new AdminCommandHandler(plugin, config, helper));
        admin.setDescription("Enable/Disable SitDown functions.");
        admin.setUsage("Usage: /<command> [enable/disable]");
        admin.setPermission(plugin.getDescription().getPermissions().get(1).getName());
        admin.setPermissionMessage(helper.transStr(config.getString("missing-permission-message")));
    }

    private void permissions() {
        helper.setSitPermission(plugin.getDescription().getPermissions().get(0));
        helper.setAdminPermission(plugin.getDescription().getPermissions().get(1));
        helper.setWildcardPermission(plugin.getDescription().getPermissions().get(2));
    }
}
