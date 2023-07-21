package me.djben.cpark.listener;

import me.djben.cpark.manager.ConfigManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.HashMap;

public class Spawn implements Listener {
    public static final HashMap<String, Location> locations = new HashMap<>();

    public static void teleportSpawn(Player player){
        if(locations.containsKey("Spawn")){
            player.teleport(locations.get("Spawn"));
        } else {
            try {
                locations.put("Spawn", (Location) ConfigManager.getData().get("Spawn"));
                player.teleport(locations.get("Spawn"));
            } catch (Exception e) {

                throw new RuntimeException(e);
            }
        }
    }
    public static void setSpawn(Player player){
        if(!locations.containsKey("Spawn")) {
            ConfigManager.getData().set("Spawn", player.getLocation());
            ConfigManager.configSave();
        } else {
            locations.remove("Spawn");
            ConfigManager.getData().set("Spawn", player.getLocation());
            ConfigManager.configSave();
        }
    }
}
