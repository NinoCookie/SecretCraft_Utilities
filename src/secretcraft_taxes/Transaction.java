package secretcraft_taxes;

import database.DBManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Transaction {
    private Economy economy;
    private Plugin plugin;
    public Transaction(Economy eco, Plugin pl) {
        economy = eco;
        plugin=pl;
    }
    public void add_money(String name, double amount){
        economy.depositPlayer(Bukkit.getOfflinePlayer(name), amount);
    }
    public void remove_money(String name, double amount){
        economy.withdrawPlayer(Bukkit.getOfflinePlayer(name), amount);
    }
    public void add_money_database(String name, double amount){
        DBManager dbManager = new DBManager();
        BukkitRunnable run = new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    if (plugin.getConfig().getString("Database.method").equalsIgnoreCase("mysql")) {
                        dbManager.MySQLConnection(plugin.getConfig().getString("Database.MySQL.hostname"), plugin.getConfig().getString("Database.MySQL.port"), plugin.getConfig().getString("Database.MySQL.database"), plugin.getConfig().getString("Database.MySQL.username"), plugin.getConfig().getString("Database.MySQL.password"));
                    } else {
                        dbManager.FileConnection(plugin);
                    }
                    dbManager.addmoney(Bukkit.getOfflinePlayer(name).getUniqueId().toString(), amount);
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
}

