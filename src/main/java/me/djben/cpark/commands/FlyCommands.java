package me.djben.cpark.commands;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

import static me.djben.cpark.utils.Chat.chat;

public class FlyCommands {
    private final ArrayList<Player> list_fly_players = new ArrayList<>();

    @CommandMethod("cpark fly <player>")
    @CommandDescription("vliegen in de wereld")
    private void commandFlyPlayer(final @Argument("player") Player player, final CommandSender sender){
        if (sender instanceof Player){
            if (sender.hasPermission("cpark.fly")){
                if (!list_fly_players.contains(player)){
                    list_fly_players.add(player);
                    player.setAllowFlight(true);
                    player.sendMessage(chat("you can fly"));
                } else if (list_fly_players.contains(player)){
                    list_fly_players.remove(player);
                    player.setAllowFlight(false);
                    player.sendMessage(chat("you cannot fly anymore."));
                }
            }
        }
    }
    @CommandMethod("cpark fly")
    @CommandDescription("vliegen in de wereld")
    private void commandFly(Player player, final CommandSender sender){
        if (sender instanceof Player){
            Player p = (Player) sender;
            if (sender.hasPermission("cpark.fly")){
                if (list_fly_players.contains(p)){
                    list_fly_players.remove(p);
                    p.setAllowFlight(false);
                    p.sendMessage(chat("you cannot fly anymore."));
                } else if (!list_fly_players.contains(p)){
                    list_fly_players.add(p);
                    p.setAllowFlight(true);
                    p.sendMessage(chat("you can fly"));
                }
            }
        }
    }
}