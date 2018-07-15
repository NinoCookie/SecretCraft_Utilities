package commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class Cmd_Reload implements CommandExecutor {
    Plugin plugin;
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender.hasPermission("su.reload")){
            plugin.reloadConfig();
            commandSender.sendMessage("Reloaded Secretcraft Utilities");
            return true;
        }
        return false;
    }

    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }
}
