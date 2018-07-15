package secretcraft_listeners;

import com.gamingmesh.jobs.config.ShopManager;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.epiceric.shopchest.ShopChest;
import de.epiceric.shopchest.event.ShopBuySellEvent;
import de.epiceric.shopchest.event.ShopCreateEvent;
import de.epiceric.shopchest.event.ShopEvent;
import de.epiceric.shopchest.listeners.ShopItemListener;
import de.epiceric.shopchest.listeners.ShopUpdateListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import secretcraft_taxes.Transaction;

public class ShopChestListener implements Listener{
    private Plugin plugin;
    private Economy eco;
    private WorldGuardPlugin worldguard;
    @EventHandler
    public void onCreate(ShopCreateEvent event) {
        if (!regionCheck(event.getShop().getLocation(), event.getPlayer())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(plugin.getConfig().getString("Messages.shop_wrong_placement"));
        }
    }
    @EventHandler
    public void onBuy(ShopBuySellEvent event){
        if(event.getShop().getVendor().getName().equalsIgnoreCase(plugin.getConfig().getString("AdminAccount.name")) && plugin.getConfig().getBoolean("AdminAccount.shopchest.emptychest")){
            InventoryHolder inventoryHolder= event.getShop().getInventoryHolder();
            inventoryHolder.getInventory().clear();
        }
        if (!event.getPlayer().hasPermission("taxsystem.notaxes") && event.getPlayer().hasPermission("taxsystem.shopchest")) {
            Transaction transaction=new Transaction(eco, plugin);
            if(plugin.getConfig().getBoolean("ShopChest.buying") && event.getType()==ShopBuySellEvent.Type.BUY){
                if(eco.getBalance(Bukkit.getOfflinePlayer(event.getPlayer().getUniqueId()))<event.getNewPrice()+(event.getNewPrice()*plugin.getConfig().getDouble("ShopChest.percentage_buying")/100)){
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(plugin.getConfig().getString("Messages.not_enough_money"));
                }
                else{
                    transaction.remove_money(event.getPlayer().getName(), event.getNewPrice()*plugin.getConfig().getDouble("ShopChest.percentage_buying")/100);
                    transaction.add_money_database(event.getPlayer().getName(), event.getNewPrice()*plugin.getConfig().getDouble("ShopChest.percentage_buying")/100);
                    if(plugin.getConfig().getBoolean("AdminAccount.use")){
                        transaction.add_money(plugin.getConfig().getString("AdminAccount.name"), event.getNewPrice()*plugin.getConfig().getDouble("ShopChest.percentage_buying")/100);
                        transaction.add_money_database(plugin.getConfig().getString("AdminAccount.name"), event.getNewPrice()*plugin.getConfig().getDouble("ShopChest.percentage_buying")/100);
                    }
                }
            }
            if(plugin.getConfig().getBoolean("ShopChest.selling") && event.getType()==ShopBuySellEvent.Type.SELL){
                if(eco.getBalance(Bukkit.getOfflinePlayer(event.getShop().getVendor().getUniqueId()))<(event.getNewPrice()*plugin.getConfig().getDouble("ShopChest.percentage_selling")/100)){
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(plugin.getConfig().getString("Messages.not_enough_money"));
                }
                else{
                    transaction.remove_money(event.getShop().getVendor().getName(), event.getNewPrice()*plugin.getConfig().getDouble("ShopChest.percentage_selling")/100);
                    transaction.add_money_database(event.getShop().getVendor().getName(), event.getNewPrice()*plugin.getConfig().getDouble("ShopChest.percentage_selling")/100);
                    if(plugin.getConfig().getBoolean("AdminAccount.use")){
                        transaction.add_money(plugin.getConfig().getString("AdminAccount.name"), event.getNewPrice()*plugin.getConfig().getDouble("ShopChest.percentage_selling")/100);
                        transaction.add_money_database(plugin.getConfig().getString("AdminAccount.name"), event.getNewPrice()*plugin.getConfig().getDouble("ShopChest.percentage_selling")/100);
                    }
                }
            }
        }
    }

    private boolean regionCheck(Location location, Player player) {
        if (worldguard != null) {
            RegionManager regionManager = worldguard.getRegionManager(location.getWorld());
            ApplicableRegionSet regionAtLocation = regionManager.getApplicableRegions(location);
            for (ProtectedRegion region : regionAtLocation) {
                if (region.getOwners().contains(player.getUniqueId()) || region.getMembers().contains(player.getUniqueId())) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }


    public void setWorldGuardPlugin(WorldGuardPlugin worldGuardPlugin) {
        this.worldguard = worldGuardPlugin;
    }

    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }

    public void setEco(Economy eco) {
        this.eco = eco;
    }
}
