package secretcraft_listeners;

import com.gamingmesh.jobs.api.JobsPaymentEvent;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import secretcraft_taxes.Transaction;

public class JobsListener implements Listener {
    private Plugin plugin;
    private Economy eco;
    @EventHandler
    public void onPayout(JobsPaymentEvent event){
        double tax=0.0;
        if (!event.getPlayer().getPlayer().hasPermission("taxsystem.notaxes") && event.getPlayer().getPlayer().hasPermission("taxsystem.jobs")) {
            Transaction transaction = new Transaction(eco, plugin);
            tax = event.getAmount() * plugin.getConfig().getDouble("Jobs.percentage") / 100;
            event.setAmount(event.getAmount() - event.getAmount() * plugin.getConfig().getDouble("Jobs.percentage") / 100);
            if (plugin.getConfig().getBoolean("AdminAccount.use")) {
                transaction.add_money(plugin.getConfig().getString("AdminAccount.name"), tax);
                transaction.add_money_database(plugin.getConfig().getString("AdminAccount.name"), tax);
            }
            transaction.add_money_database(event.getPlayer().getName(), tax);
        }
    }

    public void setEco(Economy eco) {
        this.eco = eco;
    }

    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }
}
