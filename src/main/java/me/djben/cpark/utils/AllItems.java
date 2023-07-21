package me.djben.cpark.utils;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static me.djben.cpark.utils.Chat.chat;

public class AllItems {
    private static NamespacedKey invisibleKey;

    public static void createItem(Inventory inv, Material material, int amount, short damage, boolean unbreakable, int customModelD, int invSlot, String displayName, String... loreString){
        String name = null;
        ItemStack item = null;
        List<String> lore = new ArrayList<>();
        item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES);
        meta.setUnbreakable(unbreakable);
        meta.setCustomModelData(customModelD);
        meta.setDisplayName(chat(displayName));
        if (meta instanceof Damageable){
            ((Damageable) meta).setDamage(damage);
        }
        for(String s : loreString) lore.add(chat(s));
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(invSlot - 1, item);
    }
}
