package me.djben.cpark.listener;

import me.djben.cpark.manager.AFKManager;
import me.djben.cpark.manager.ConfigManager;
import me.djben.cpark.utils.AllItems;
import me.djben.cpark.utils.MenuGui;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

import static me.djben.cpark.utils.Chat.chat;

public class JoinEvent implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        String title = ConfigManager.getData().getString("World.WelcomeTitle");
        String subTitle = ConfigManager.getData().getString("World.welcomeSubTitle");
        p.sendTitle(chat(title), chat(subTitle), 5, 300, 5);
        AFKManager.playerJoined(event.getPlayer());
        AllItems.createItem(event.getPlayer().getInventory(), Material.MINECART, 1, (short) 0, false, 0,1, "&1attractie", "hey", "hey");
        if (!p.hasPermission("crazycraft.player")) {
            p.getInventory().clear();
            AllItems.createItem(event.getPlayer().getInventory(), Material.MINECART, 1, (short) 0, false, 0, 1, "&1attractie", "hey", "hey");
            Player player = event.getPlayer();
            Spawn.teleportSpawn(player);
        }
        String joinMessage = ConfigManager.getData().getString("Message.join");
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(chat(joinMessage + p.getDisplayName())));
        //event.setJoinMessage(chat(joinMessage + p.getDisplayName()));
    }
    @EventHandler
    public void onPlayerQuit(PlayerMoveEvent e){
        AFKManager.playerLeft(e.getPlayer());
    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        AFKManager.playerMoved(e.getPlayer());
    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            ItemStack currentItem = p.getInventory().getItemInMainHand();
            //We can just return this if it is air or null
            if (currentItem == null || currentItem.getType() == Material.AIR) return;
            //We want to make sure that the item HAS item meta and a display name before we try to access it.
            if (!currentItem.hasItemMeta() || (currentItem.hasItemMeta() && !Objects.requireNonNull(currentItem.getItemMeta()).hasDisplayName())){
                return;
            }
            if(Objects.requireNonNull(currentItem.getItemMeta()).getDisplayName().equals(chat("&1attractie"))){
                currentItem.getItemMeta().getDisplayName().equals(chat("&1attractie"));

                //Let's cancel the event, just because.
                e.setCancelled(true);
                //Let's also update the player's inventory. This isn't really necessary for a compass but it's a good practice to use for more complex things.
                e.getPlayer().updateInventory();
                //open inventory
                try {
                    p.openInventory(MenuGui.GUI(p));
                } catch (UnsupportedEncodingException unsupportedEncodingException) {
                    unsupportedEncodingException.printStackTrace();
                }
            }
        }
    }
}
