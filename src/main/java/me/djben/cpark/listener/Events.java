package me.djben.cpark.listener;

import me.djben.cpark.utils.MenuGui;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;

public class Events implements Listener {
    public Events() {
    }

    @Deprecated
    @EventHandler
    public void events(InventoryClickEvent e) {
        InventoryView view = e.getView();
        String title = view.getTitle();
        Player player = (Player) e.getWhoClicked();
        if (title.equals(MenuGui.inventory_name)){
            e.setCancelled(true);
            player.updateInventory();
            if (e.getCurrentItem() == null){
                return;
            }
            if (title.equals(MenuGui.inventory_name)){
                MenuGui.clicked(player, e.getRawSlot(), e.getCurrentItem(), e.getInventory());
                player.closeInventory();
            }
        }
    }
}
