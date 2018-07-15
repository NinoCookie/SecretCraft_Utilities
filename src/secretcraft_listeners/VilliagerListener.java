package secretcraft_listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.entity.VillagerReplenishTradeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class VilliagerListener implements Listener {
    @EventHandler
    public void onTrade(VillagerAcquireTradeEvent event){
        if(event.getEntity().getCareer().getProfession().name().equalsIgnoreCase("Librarian")){
            Bukkit.getConsoleSender().sendMessage(""+event.getRecipe().getResult().getType().name());
            Bukkit.getConsoleSender().sendMessage(""+event.getRecipe().getResult().getType());




        }
    }
    @EventHandler
    public void onTr(PlayerInteractEntityEvent event){
        if(event.getRightClicked().getType()== EntityType.VILLAGER){
            //Geht eh nicht fml
            Villager villager=event.getRightClicked().getEntityId();


        }
    }
}
