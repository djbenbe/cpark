package me.djben.cpark.manager;

import me.djben.cpark.Cpark;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class ConfigManager {
    private static Cpark plugin;
    private static FileConfiguration menuConfig;
    private static File menuFile = null;
    private static FileConfiguration dataConfig;
    private static File configFile = null;

    public ConfigManager(Cpark p) {
        plugin = p;
        //saves|initializes the config
        saveDefaultConfig();

    }

    public static void reloadMenu(){
        if (menuFile == null) {
            menuFile = new File(plugin.getDataFolder(), "menu.yml");
        }

        menuConfig = YamlConfiguration.loadConfiguration(menuFile);
        InputStream defaultStream = plugin.getResource("menu.yml");
        if(defaultStream != null){
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
        }
    }
    public static void reloadConfig(){
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), "config.yml");
        }

        dataConfig = YamlConfiguration.loadConfiguration(configFile);
        InputStream defaultStream1 = plugin.getResource("config.yml");
        if(defaultStream1 != null){
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream1));
        }
    }


    public static FileConfiguration getMenu(){
        if (menuConfig == null)
            reloadMenu();
        return menuConfig;
    }
    public static FileConfiguration getData(){
        if (dataConfig == null)
            reloadConfig();
        return dataConfig;
    }

    public static void menuSave(){
        if (menuConfig == null | menuFile == null)
            return;
        try {
            menuConfig.save(menuFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + menuConfig, e);
        }
    }
    public static void configSave(){
        if (dataConfig == null | configFile == null)
            return;
        try {
            dataConfig.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + dataConfig, e);
        }
    }
    public void saveDefaultConfig(){
        if (menuFile == null) {
            menuFile = new File(plugin.getDataFolder(), "menu.yml");
        }
        if (!menuFile.exists()){
            plugin.saveResource("menu.yml", false);
        }
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), "config.yml");
        }
        if (!configFile.exists()){
            plugin.saveResource("config.yml", false);
        }
    }

}