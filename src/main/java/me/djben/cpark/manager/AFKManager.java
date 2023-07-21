package me.djben.cpark.manager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class AFKManager {

    private static final long MOVEMENT_THRESHOLD = 60000L;

    private static final HashMap<Player, Long> lastMovement = new HashMap<>();
    private static final HashMap<Player, Boolean> previousData = new HashMap<>();

    public static void playerJoined(Player player){
        lastMovement.put(player, System.currentTimeMillis());
    }

    public static void playerLeft(Player player){
        lastMovement.remove(player);
    }

    public static boolean toggleAFKStatus(Player player){

        if (isAFK(player)){
            previousData.put(player, false);
            lastMovement.put(player, System.currentTimeMillis());
            return false;
        }else{
            previousData.put(player, true);
            lastMovement.put(player, -1L);
            return true;
        }

    }

    public static void playerMoved(Player player){

        lastMovement.put(player, System.currentTimeMillis());

        checkPlayerAFKStatus(player);

    }

    public static boolean isAFK(Player player){

        if(lastMovement.containsKey(player)){
            if(lastMovement.get(player) == -1L){
                return true;
            }else{
                long timeElapsed = System.currentTimeMillis() - lastMovement.get(player);

                //see if they have moved since 1 minute
                //60000
                return timeElapsed >= MOVEMENT_THRESHOLD;
            }
        }else{
            lastMovement.put(player, System.currentTimeMillis());
        }

        return false;
    }

    public static void checkAllPlayersAFKStatus(){

        for (Map.Entry<Player, Long> entry : lastMovement.entrySet()){
            checkPlayerAFKStatus(entry.getKey());
        }

    }

    public static void checkPlayerAFKStatus(Player player){
        if (lastMovement.containsKey(player)){

            boolean nowAFK = isAFK(player);

            if (previousData.containsKey(player)){

                boolean wasAFK = previousData.get(player);

                if(wasAFK && !nowAFK){
                    player.sendMessage("You are no longer AFK");
                    previousData.put(player, false);
                }else if(!wasAFK && nowAFK){
                    player.sendMessage("You are now AFK!");
                    previousData.put(player, true);
                }

            }else{
                previousData.put(player, nowAFK);
            }

        }
    }

    public static void announceToOthers(Player target, boolean isAFK){

        Bukkit.getServer().getOnlinePlayers().stream()
                .forEach(player -> {
//                    if(!player.equals(target)){
                    if (isAFK){
                        player.sendMessage(target.getDisplayName() + " is now AFK.");
                    }else{
                        player.sendMessage(target.getDisplayName() + " is no longer AFK.");
                        //}
                    }
                });

    }

}