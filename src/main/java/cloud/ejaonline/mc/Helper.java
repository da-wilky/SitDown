package cloud.ejaonline.mc;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

public class Helper {
    private JavaPlugin plugin;
    private FileConfiguration config;

    Helper(JavaPlugin plugin, FileConfiguration config) {
        this.plugin = plugin;
        this.config = config;
    }

    public String transStr(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    };

    public void print(String str) {
        plugin.getLogger().info(str);
    };

    String vehicleMessage = transStr(config.getString("vehicle-air-message"));
    String denyMessage = transStr(config.getString("missing-permission-message"));
    String sitMessage = transStr(config.getString("sit-message"));

    public void sitDown(Player p, Location location) {
        if (!p.hasPermission(SitDown.sitPermission)) {
            p.sendMessage(denyMessage);
            return;
        } else if (p.isInsideVehicle() || !p.isOnGround()) {
            // isOnGround deprecated cuz data from client = vulnurable for hax - not that
            // important in this case ^^
            p.sendMessage(vehicleMessage);
            return;
        }

        ArmorStand chair = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        chair.setMetadata("chair", new FixedMetadataValue(plugin, "chair"));

        chair.setGravity(false);
        chair.setVisible(false);

        chair.addPassenger(p);
        p.sendMessage(sitMessage);
    };
}
