package cloud.ejaonline.mc.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import cloud.ejaonline.mc.SitDown;

public class CommandHandler implements CommandExecutor {
    private FileConfiguration config;
    private SitDown plug;

    public CommandHandler(FileConfiguration config, SitDown plug) {
        this.config = config;
        this.plug = plug;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] strings) {
        if (!plug.pluginEnabled) {
            sender.sendMessage(plug.transStr(config.getString("disabled-message")));
            return true;
        } else if (!(sender instanceof Player)) {
            sender.sendMessage(plug.transStr(config.getString("wrong-sender-message")));
            return true;
        }

        Player p = (Player) sender;
        Location location = p.getLocation();
        location.setY(location.getBlockY() - (1.7 - location.getY() % 1));

        plug.sitDown(p, location);

        return true;
    }
}
