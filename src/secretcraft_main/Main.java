package secretcraft_main;


import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import commands.Cmd_TaxSystem;
import commands.Cmd_su;
import commands.Cmd_usertaxes;
import database.DBManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import secretcraft_listeners.*;

import java.io.File;
import java.sql.SQLException;

public class Main extends JavaPlugin {
    private Economy economy; //Vault economy Object
    private DBManager dbManager = new DBManager(); //DBManager Object
    @Override
    public void onEnable(){
        initializeInfo();
        initializeConfig(); //initialisieren der config, ob diese existiert, wenn nicht wird sie neu erstellt
        initialize_DataBase(); //initialisieren der datenbank, erstellt die table falls noch nicht vorhanden
        if(!setupEconomy()){ //prüft ob vault aktiv ist und initialisiert das economy object
            Bukkit.getConsoleSender().sendMessage("§cDa ist was mit Vault schief gelaufen!"); //schießt nen fehler wenn vault nicht passt
        }
        if(economy!=null){ //wenn vault okay ist
            //schreibt in die Console das das plugin initialisiert ist.
            Bukkit.getConsoleSender().sendMessage("§2SecretCraft_Utilities");

            //Hier ist der ShopChest Listener, dieser kümmert sich um alle Events von ShopChest
            if(this.getConfig().getBoolean("ShopChest.enable")){
                ShopChestListener shopChestListener =new ShopChestListener(); //Initialisiert den listener für ShopChestListener
                shopChestListener.setWorldGuardPlugin(getWorldGuard()); //setzt worldguardplugin variable um auf worldguard zu referieren
                shopChestListener.setEco(economy);
                shopChestListener.setPlugin(this);
                Bukkit.getPluginManager().registerEvents(shopChestListener, this); //Registriert das Event
            }

            //Hier ist der Jobs Listener, dieser kümmert sich um alle Events von Jobs
            if(this.getConfig().getBoolean("Jobs.enable")){
                JobsListener jobsListener=new JobsListener();
                jobsListener.setEco(economy);
                jobsListener.setPlugin(this);
                Bukkit.getPluginManager().registerEvents(jobsListener, this);
            }

            //Hier ist der PlayerJoins Listener, initialisiert die spieler in die datenbank etc.
            PlayerJoinsListener playerJoinsListener=new PlayerJoinsListener();
            playerJoinsListener.setPlugin(this);
            Bukkit.getPluginManager().registerEvents(playerJoinsListener, this);

            //Hier ist der SquaredPlot Listener, dieser kümmert sich um alle Events von SquaredPlot
            if(this.getConfig().getBoolean("PlotSquared.enable")){
                PlotSquaredListener plotSquaredListener=new PlotSquaredListener();
                plotSquaredListener.setEco(economy);
                plotSquaredListener.setPlugin(this);
                Bukkit.getPluginManager().registerEvents(plotSquaredListener, this);
            }

            //Commands:
            //Steuern anzeigen etc. Befehl(e)
            Cmd_usertaxes usertaxes=new Cmd_usertaxes();
            usertaxes.setPlugin(this);
            this.getCommand("taxes").setExecutor(usertaxes);
            //Standart info für Steuersystem
            Cmd_TaxSystem taxSystem=new Cmd_TaxSystem();
            taxSystem.setList(this.getConfig().getStringList("Messages.taxsystem_info"));
            this.getCommand("taxsystem").setExecutor(taxSystem);
            //Reload command
            Cmd_su cmd_su=new Cmd_su();
            cmd_su.setPlugin(this);
            this.getCommand("su").setExecutor(cmd_su);
        }
    }

    private WorldGuardPlugin getWorldGuard() { //gibt WorldGuard Referenz zurück
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }
        return (WorldGuardPlugin) plugin;
    }
    private void initializeConfig(){
        File file=new File(getDataFolder()+File.separator+"config.yml");
        if(!file.exists()){
            this.saveDefaultConfig();
        }
        this.reloadConfig();
    }
    private void initializeInfo(){
        File file=new File(getDataFolder()+File.separator+"info.txt");
        if(!file.exists()){
            this.saveResource("info.txt", true);
        }
    }
    private boolean setupEconomy(){
        if(getServer().getPluginManager().getPlugin("Vault")==null){
            return false;
        }
        RegisteredServiceProvider<Economy> rsp= getServer().getServicesManager().getRegistration(Economy.class);
        if(rsp==null){
            return false;
        }
        economy=rsp.getProvider();
        return true;
    }
    private void initialize_DataBase(){
        try{
            if(this.getConfig().getString("Database.method").equalsIgnoreCase("mysql")){
                dbManager.MySQLConnection(this.getConfig().getString("Database.MySQL.hostname"), this.getConfig().getString("Database.MySQL.port"), this.getConfig().getString("Database.MySQL.database"), this.getConfig().getString("Database.MySQL.username"), this.getConfig().getString("Database.MySQL.password"));
            }
            else{
                dbManager.FileConnection(this);
            }
            dbManager.createtable();
        }catch (SQLException e){
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                dbManager.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
