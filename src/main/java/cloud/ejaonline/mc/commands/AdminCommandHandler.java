package cloud.ejaonline.mc.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import cloud.ejaonline.mc.Helper;
import cloud.ejaonline.mc.SitDown;

public class AdminCommandHandler implements CommandExecutor {
    private JavaPlugin plugin;
    private FileConfiguration config;
    private Helper helper;

    public AdminCommandHandler(JavaPlugin plugin, FileConfiguration config, Helper helper) {
        this.config = config;
        this.helper = helper;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] strings) {
        if (!(sender.hasPermission(SitDown.adminPermission) || sender.hasPermission(SitDown.wildcardPermission))) {
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

        if (!(newEnabled ^ SitDown.pluginEnabled)) {
            sender.sendMessage(
                    helper.transStr(String.format(config.getString("already-disabled-message"), strings[0])));
        } else {
            config.set("enabled", newEnabled);
            SitDown.pluginEnabled = newEnabled;
            plugin.saveConfig();
            sender.sendMessage(helper.transStr(String.format(config.getString("now-disabled-message"), strings[0])));
        }
        return true;
    }
}
