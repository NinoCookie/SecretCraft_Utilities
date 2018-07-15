package commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class Cmd_TaxSystem implements CommandExecutor {
    List<String> list=new ArrayList<>();
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender.hasPermission("taxsystem.see")){
            commandSender.sendMessage("§c[SteuerSystem]&f Version: 2.1 von Nino");
            if(list!=null){
                for (int i = 0; i < list.size(); i++) {
                    commandSender.sendMessage(list.get(i));
                }
            }
            return true;
        }
        return false;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
