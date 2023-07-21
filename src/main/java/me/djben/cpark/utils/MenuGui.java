package me.djben.cpark.utils;

import me.djben.cpark.manager.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.Set;

import static me.djben.cpark.utils.Chat.chat;

public class MenuGui {
    public static Inventory inv;
    public static String inventory_name;
    public static int inv_rows = 4 * 9;

    public static void initialize(){
        String inventoryName;
        inventoryName = ConfigManager.getData().getString("Message.nameInventory");
        inventory_name = chat(inventoryName);
        inv = Bukkit.createInventory(null, inv_rows);
    }

    public static Inventory GUI(Player p) throws UnsupportedEncodingException {
        int damage = 0;
        int customData = 0;
        boolean unbreakable = false;
        String material = null;
        String name = null;
        int slot = 0;
        String lore1 = null;
        String lore = null;
        Set<String> keyB = ConfigManager.getMenu().getConfigurationSection("item").getKeys(false);
        for (String key : keyB) {
            //List<menuData> items = new ArrayList<>();
            damage = ConfigManager.getMenu().getInt("item." + key + ".damage");
            unbreakable = ConfigManager.getMenu().getBoolean("item." + key + ".unbreakable");
            customData = ConfigManager.getMenu().getInt("item." + key + ".customdata");
            material = ConfigManager.getMenu().getString("item." + key + ".material");
            name = ConfigManager.getMenu().getString(chat("item." + key + ".name"));
            slot = ConfigManager.getMenu().getInt("item." + key + ".slot");
            lore1 = ConfigManager.getMenu().getString("item." + key + ".lore");
            lore = ConfigManager.getMenu().getString("item." + key + ".open");
            //items.add(new menuData(key));
            AllItems.createItem(inv, Material.valueOf(material), 1, (short) damage, unbreakable, customData ,slot, name, nameOpen(lore), lore1);
        }
        Inventory toReturn = Bukkit.createInventory(null, inv_rows, inventory_name);

        toReturn.setContents(inv.getContents());
        return toReturn;
    }


    public static void clicked(Player player, int slot, org.bukkit.inventory.ItemStack click, Inventory inv){
        int  x = 0;
        int  y = 0;
        int  z = 0;
        String name, open = null;
        Set<String> keyB = ConfigManager.getMenu().getConfigurationSection("item").getKeys(false);
        for (String key : keyB) {
            x = ConfigManager.getMenu().getInt("item."+key+".x");
            y = ConfigManager.getMenu().getInt("item."+key+".y");
            z = ConfigManager.getMenu().getInt("item."+key+".z");
            name = ConfigManager.getMenu().getString("item."+key+".name");
            open = ConfigManager.getMenu().getString("item." +key+ "open");
            if (Objects.equals(open, "open")) {
                if (Objects.requireNonNull(click.getItemMeta()).getDisplayName().equals(name)){
                    Location newLocation = new Location(player.getWorld(), x, y, z);
                    player.teleport(newLocation);
                }
            }

        }

    }
    public static String nameOpen(String openMsg){
        String openMessage = null;
        String open = ConfigManager.getData().getString("Message.open");
        String close = ConfigManager.getData().getString("Message.close");
        String maintenance = ConfigManager.getData().getString("Message.maintenance");
        String construction = ConfigManager.getData().getString("Message.construction");
        String automatic = ConfigManager.getData().getString("Message.automatic");
        switch (openMsg) {
            case "open":
                openMessage = chat(open);
                break;
            case "close":
                openMessage = chat(close);
                break;
            case "maintenance":
                openMessage = chat(maintenance);
                break;
            case "construction":
                openMessage = chat(construction);
                break;
            case "automatic":
                openMessage = chat(automatic);
                break;
        }
        return openMessage;
    }
}