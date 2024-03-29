package cloud.ejaonline.mc.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.block.data.type.Stairs.Shape;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;
import org.spigotmc.event.entity.EntityDismountEvent;

import cloud.ejaonline.mc.SitDown;

public class EventsHandler implements Listener {
    private SitDown plug;
    private String standMessage;
    private boolean stairs;

    public EventsHandler(FileConfiguration config, SitDown plug) {
        this.plug = plug;
        standMessage = plug.transStr(config.getString("stand-message"));
        stairs = config.getBoolean("right-click-stair-sit");
    }

    @EventHandler
    public void onEntityDismount(EntityDismountEvent e) {
        if (!(e.getEntity() instanceof Player))
            return;

        else if (!(e.getDismounted() instanceof ArmorStand))
            return;

        if (e.getDismounted().getMetadata("chair") != null) {
            e.getDismounted().remove();
            Player p = (Player) e.getEntity();
            p.teleport(p.getLocation().add(0, 1, 0));
            p.sendMessage(standMessage);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (p.isInsideVehicle() && p.getVehicle().getMetadata("chair") != null) {
            p.getVehicle().remove();
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if ((!stairs) || (!plug.pluginEnabled)
                || (!(e.getPlayer().hasPermission(plug.getSitPermission())
                        || e.getPlayer().hasPermission(plug.getWildcardPermission())))
                || (e.getClickedBlock() == null) || e.getPlayer().isSneaking()
                || e.getHand() == EquipmentSlot.OFF_HAND
                || e.getPlayer().getInventory().getItemInMainHand().getType() != Material.AIR
                || e.getClickedBlock().getLocation().distance(e.getPlayer().getLocation()) > 3.5)
            return;

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getClickedBlock().getBlockData() instanceof Stairs) {
                Stairs stairs = (Stairs) e.getClickedBlock().getBlockData();
                if (!e.getPlayer().getFacing().equals(stairs.getFacing())
                        || e.getPlayer().getEyeLocation().getPitch() < -4 || stairs.getHalf().equals(Half.TOP)
                        || !stairs.getShape().equals(Shape.STRAIGHT) || stairs.isWaterlogged()) {
                    return;
                }
                BlockFace facing = stairs.getFacing().getOppositeFace();

                Location location = e.getClickedBlock().getLocation();

                location.add(0.5, -1.2, 0.5);
                location.setDirection(new Vector(facing.getModX(), facing.getModY(), facing.getModZ()));

                plug.sitDown(e.getPlayer(), location);
            }
        }
    }
}
