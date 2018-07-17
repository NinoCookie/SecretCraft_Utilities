package commands;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Cmd_su implements CommandExecutor {
    private Plugin plugin;
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings.length>0){
            if(strings[0].equalsIgnoreCase("reload")){
                if(commandSender instanceof Player && commandSender.hasPermission("su.reload")){
                    plugin.reloadConfig();
                    commandSender.sendMessage("Reloaded Secretcraft Utilities");
                    return true;
                }
            }
            if(strings[0].equalsIgnoreCase("gm3")){
                if(commandSender instanceof Player && commandSender.hasPermission("su.gm3")){
                    if(((Player) commandSender).getGameMode()==GameMode.SPECTATOR){
                        ((Player) commandSender).setGameMode(GameMode.SURVIVAL);
                        return true;
                    }
                    else{
                        ((Player) commandSender).setGameMode(GameMode.SPECTATOR);
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }
}
