package cloud.ejaonline.mc;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import cloud.ejaonline.mc.commands.AdminCommandHandler;
import cloud.ejaonline.mc.commands.CommandHandler;
import cloud.ejaonline.mc.events.EventsHandler;

public class SitDown extends JavaPlugin {
    private FileConfiguration config;

    private String vehicleMessage;
    private String denyMessage;
    private String sitMessage;

    private Permission sitPermission;
    private Permission adminPermission;
    private Permission wildcardPermission;

    public boolean pluginEnabled;

    @Override
    public void onEnable() {
        // Variables
        saveDefaultConfig();
        config = getConfig();

        vehicleMessage = transStr(config.getString("vehicle-air-message"));
        denyMessage = transStr(config.getString("missing-permission-message"));
        sitMessage = transStr(config.getString("sit-message"));
        pluginEnabled = config.getBoolean("enabled");

        // Commands and Permissions
        setCommands();
        setPermissions();

        // Events
        getServer().getPluginManager().registerEvents(new EventsHandler(config, this), this);
    }

    @Override
    public void onDisable() {
        // Disable
        
    }

    public void print(String str) {
        this.getLogger().info(str);
    };

    public String transStr(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public void sitDown(Player p, Location location) {
        if (!(p.hasPermission(sitPermission) || p.hasPermission(wildcardPermission))) {
            p.sendMessage(denyMessage);
            return;
        } else if (p.isInsideVehicle() || !p.isOnGround()) {
            // isOnGround deprecated cuz data from client = vulnurable for hax - not that
            // important in this case ^^
            p.sendMessage(vehicleMessage);
            return;
        }

        location.getWorld().spawn(location, ArmorStand.class, armorStand -> {
            armorStand.setGravity(false);
            armorStand.setVisible(false);
            armorStand.setMetadata("chair", new FixedMetadataValue(this, "chair"));
            armorStand.addPassenger(p);
        });
        p.sendMessage(sitMessage);
    }

    private void setCommands() {
        String missingPermString = transStr(config.getString("missing-permission-message"));

        PluginCommand sit = getCommand("sit");
        sit.setExecutor(new CommandHandler(config, this));
        sit.setDescription("Sit down.");
        sit.setUsage("Usage: /<command>");
        sit.setPermission(getDescription().getPermissions().get(0).getName());
        sit.setPermissionMessage(missingPermString);

        PluginCommand admin = getCommand("sitdown");
        admin.setExecutor(new AdminCommandHandler(this, config));
        admin.setDescription("Enable/Disable SitDown functions.");
        admin.setUsage("Usage: /<command> [enable/disable]");
        admin.setPermission(getDescription().getPermissions().get(1).getName());
        admin.setPermissionMessage(missingPermString);
    }

    private void setPermissions() {
        sitPermission = getDescription().getPermissions().get(0);
        adminPermission = getDescription().getPermissions().get(1);
        wildcardPermission = getDescription().getPermissions().get(2);
    }

    public Permission getSitPermission() {
        return sitPermission;
    }

    public Permission getAdminPermission() {
        return adminPermission;
    }

    public Permission getWildcardPermission() {
        return wildcardPermission;
    }
}