package cloud.ejaonline.mc;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

public class Helper {
    private JavaPlugin plugin;
    private String vehicleMessage;
    private String denyMessage;
    private String sitMessage;

    private Permission sitPermission;
    private Permission adminPermission;
    private Permission wildcardPermission;

    private boolean pluginEnabled;

    public Helper(JavaPlugin plugin, FileConfiguration config) {
        // this.config = config;
        this.plugin = plugin;
        vehicleMessage = transStr(config.getString("vehicle-air-message"));
        denyMessage = transStr(config.getString("missing-permission-message"));
        sitMessage = transStr(config.getString("sit-message"));
        pluginEnabled = config.getBoolean("enabled");
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
            armorStand.setMetadata("chair", new FixedMetadataValue(plugin, "chair"));
            armorStand.addPassenger(p);
        });
        p.sendMessage(sitMessage);
    };

    public String transStr(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    };

    public void print(String str) {
        plugin.getLogger().info(str);
    };

    public void setPluginEnabled(boolean bool) {
        pluginEnabled = bool;
    }

    public boolean getPluginEnabled() {
        return pluginEnabled;
    }

    protected void setSitPermission(Permission p) {
        sitPermission = p;
    }

    protected void setAdminPermission(Permission p) {
        adminPermission = p;
    }

    protected void setWildcardPermission(Permission p) {
        wildcardPermission = p;
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
