package cloud.ejaonline.mc.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import cloud.ejaonline.mc.SitDown;

public class AdminCommandHandler implements CommandExecutor {
    private SitDown plug;
    private FileConfiguration config;

    public AdminCommandHandler(SitDown plug, FileConfiguration config) {
        this.config = config;
        this.plug = plug;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] strings) {
        if (!(sender.hasPermission(plug.getAdminPermission())
                || sender.hasPermission(plug.getWildcardPermission()))) {
            return false;
        }
        if (strings.length != 1) {
            return false;
        }

        boolean newEnabled;
        if (strings[0].equalsIgnoreCase("enable")) {
            newEnabled = true;
        } else if (strings[0].equalsIgnoreCase("disable")) {
            newEnabled = false;
        } else {
            return false;
        }

        if (!(newEnabled ^ plug.pluginEnabled)) {
            sender.sendMessage(
                    plug.transStr(String.format(config.getString("already-disabled-message"), strings[0])));
        } else {
            config.set("enabled", newEnabled);
            plug.pluginEnabled = newEnabled;
            plug.saveConfig();
            sender.sendMessage(plug.transStr(String.format(config.getString("now-disabled-message"), strings[0])));
        }
        return true;
    }
}
