package secretcraft_listeners;

import database.DBManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerJoinsListener implements Listener {
    private Plugin plugin;
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        DBManager dbManager=new DBManager();
        BukkitRunnable run=new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    if (plugin.getConfig().getString("Database.method").equalsIgnoreCase("mysql")) {
                        dbManager.MySQLConnection(plugin.getConfig().getString("Database.MySQL.hostname"), plugin.getConfig().getString("Database.MySQL.port"), plugin.getConfig().getString("Database.MySQL.database"), plugin.getConfig().getString("Database.MySQL.username"), plugin.getConfig().getString("Database.MySQL.password"));
                    } else {
                        dbManager.FileConnection(plugin);
                    }
                    String sql = "select count(*) from taxsystemplayers where uuid='" + player.getUniqueId().toString() + "'";
                    if (!dbManager.contains(sql)) {
                        dbManager.registerplayer(player.getUniqueId().toString());
                    }
                }catch (Exception e){
                    Bukkit.getConsoleSender().sendMessage("§cTaxSystem - Error on using or connecting to database!");
                    Bukkit.getConsoleSender().sendMessage("§cTaxSystem - Check configuration or database if ur using mysql!");
                    e.printStackTrace();
                }
                finally {
                    try {
                        dbManager.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        };
        run.runTaskAsynchronously(plugin);
    }

    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }

}
