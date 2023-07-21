package me.djben.cpark.commands;

import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import me.djben.cpark.manager.AFKManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AFKCommands {

    @CommandMethod("cpark isAFK")
    @CommandDescription("hij is even weg")
    private void isCommandAFK(final Player player, final CommandSender sender, String[] args){
        if(args.length == 0){
            if (AFKManager.isAFK(player)){
                player.sendMessage("You are currently AFK");
            } else {
                player.sendMessage("You are not currently AFK");
            }
        } else {
            Player target = Bukkit.getPlayerExact(args[0]);
            assert target != null;
            if (AFKManager.isAFK(target)){
                player.sendMessage(target.getDisplayName() + " is currently AFK.");
            } else {
                player.sendMessage(target.getDisplayName() + " is not currently AFK");

            }
        }
    }
    @CommandMethod("cpark AFK")
    @CommandDescription("hij is even weg")
    private void commandAFK(final Player player, final CommandSender sender) {
        if (AFKManager.toggleAFKStatus(player)){
            player.sendMessage("You are now AFK.");
            //TODO - announcement to everyone else
        } else {
            player.sendMessage("You are no longer AFK");
            //TODO - announcement to everyone else
        }
    }
}