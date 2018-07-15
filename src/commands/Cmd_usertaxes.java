package commands;

import database.DBManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.util.regex.Pattern;

public class Cmd_usertaxes implements CommandExecutor {
    private String msg;
    private String msg2;
    private Plugin plugin;
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender.hasPermission("taxsystem.showtaxes")) {
            BukkitRunnable run = new BukkitRunnable() {
                @Override
                public void run() {
                    DBManager dbManager = new DBManager();
                    try {
                        if (plugin.getConfig().getString("Database.method").equalsIgnoreCase("mysql")) {
                            dbManager.MySQLConnection(plugin.getConfig().getString("Database.MySQL.hostname"), plugin.getConfig().getString("Database.MySQL.port"), plugin.getConfig().getString("Database.MySQL.database"), plugin.getConfig().getString("Database.MySQL.username"), plugin.getConfig().getString("Database.MySQL.password"));
                        } else {
                            dbManager.FileConnection(plugin);
                        }
                        boolean quest=false;
                        if(commandSender instanceof Player){
                            if(strings.length>0){
                                String sql = "select count(*) from taxsystemplayers where uuid='" + Bukkit.getOfflinePlayer(strings[0]).getUniqueId().toString() + "'";
                                if(dbManager.contains(sql)){
                                    Player player= ((Player) commandSender).getPlayer();
                                    if(player.hasPermission("taxsystem.showtaxes_others")){
                                        player.sendMessage(msg2.replaceAll(Pattern.quote("%money%"),  ""+Math.round(dbManager.usertaxes(Bukkit.getOfflinePlayer(strings[0]).getUniqueId().toString()))).replaceAll(Pattern.quote("%player%"), Bukkit.getOfflinePlayer(strings[0]).getName()));
                                    }
                                    else{
                                        player.sendMessage(plugin.getConfig().getString("Messages.permission"));
                                    }
                                    quest=true;
                                }
                                switch (strings[0]){
                                    case "insgesamt":
                                    case "all":
                                        if(commandSender instanceof Player) {
                                            Player player = ((Player) commandSender).getPlayer();
                                            if (player.hasPermission("taxsystem.showtaxes_all")) {
                                                player.sendMessage(msg.replaceAll(Pattern.quote("%money%"), "" + Math.round(dbManager.alltaxes())));
                                            }
                                            else{
                                                player.sendMessage(plugin.getConfig().getString("Messages.permission"));
                                            }
                                            quest=true;
                                        }
                                        break;
                                }
                                if(!quest){
                                    if(commandSender instanceof Player) {
                                        if (commandSender.hasPermission("taxsystem.showtaxes_error")) {
                                            commandSender.sendMessage(plugin.getConfig().getString("Messages.show_wrong_para"));
                                        }
                                        else{
                                            commandSender.sendMessage(plugin.getConfig().getString("Messages.permission"));
                                        }
                                    }
                                }
                            }
                            else{
                                Player player = ((Player) commandSender).getPlayer();
                                player.sendMessage(msg.replaceAll(Pattern.quote("%money%"), "" + Math.round(dbManager.usertaxes(player.getUniqueId().toString()))));
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            dbManager.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            run.runTaskAsynchronously(plugin);
        }
        return true;
    }

    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
        this.msg = this.plugin.getConfig().getString("Messages.show_taxes");
        this.msg2 = this.plugin.getConfig().getString("Messages.show_other_taxes");
    }
}
