package me.djben.cpark.utils;

import me.djben.cpark.manager.ConfigManager;

import static me.djben.cpark.utils.Chat.chat;

public class MenuData {
    private String name, item, lore,lore1, material;
    private int damage, slot, customData;
    private boolean unbreakable;

    public MenuData(String item){
        damage = ConfigManager.getMenu().getInt("item." + item + ".damage");
        unbreakable = ConfigManager.getMenu().getBoolean("item." + item + ".unbreakable");
        customData = ConfigManager.getMenu().getInt("item." + item + ".customdata");
        material = ConfigManager.getMenu().getString("item." + item + ".material");
        name = ConfigManager.getMenu().getString(chat("item." + item + ".name"));
        slot = ConfigManager.getMenu().getInt("item." + item + ".slot");
        lore1 = ConfigManager.getMenu().getString("item." + item + ".lore");
        lore = ConfigManager.getMenu().getString("item." + item + ".open");
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getLore() {
        return lore;
    }

    public void setLore(String lore) {
        this.lore = lore;
    }

    public String getLore1() {
        return lore1;
    }

    public void setLore1(String lore1) {
        this.lore1 = lore1;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public boolean isUnbreakable() {
        return unbreakable;
    }

    public void setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
    }
}
