package secretcraft_listeners;

import com.plotsquared.bukkit.events.PlayerClaimPlotEvent;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import secretcraft_taxes.Transaction;

public class PlotSquaredListener implements Listener {
    private Plugin plugin;
    private Economy eco;
    @EventHandler
    public void onPlot_Claim(PlayerClaimPlotEvent event){
        if(eco!=null){
            if(plugin.getConfig().getBoolean("AdminAccount.use")){
                Transaction transaction=new Transaction(eco, plugin);
                transaction.add_money(plugin.getConfig().getString("AdminAccount.name"), plugin.getConfig().getDouble("PlotSquaredListener.amount"));
                transaction.add_money_database(plugin.getConfig().getString("AdminAccount.name"), plugin.getConfig().getDouble("PlotSquaredListener.amount"));
            }
        }
    }

    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }

    public void setEco(Economy eco) {
        this.eco = eco;
    }
}
