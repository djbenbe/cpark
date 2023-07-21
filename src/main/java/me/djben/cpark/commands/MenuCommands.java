package me.djben.cpark.commands;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import me.djben.cpark.manager.ConfigManager;
import me.djben.cpark.permissions.CommandPermission;
import me.djben.cpark.permissions.Permissions;
import me.djben.cpark.utils.MenuGui;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.UnsupportedEncodingException;
import java.util.Set;

import static me.djben.cpark.listener.Spawn.setSpawn;
import static me.djben.cpark.listener.Spawn.teleportSpawn;
import static me.djben.cpark.utils.Chat.chat;
import static me.djben.cpark.utils.LightApiProperty.changeLights;

public class MenuCommands {
    @CommandPermission(Permissions.COMMAND_MENU)
    @CommandMethod("menu")
    @CommandDescription("Deze commando gaat de menu open")
    private void commandMenu(final Player player, final CommandSender sender) {
        try {
            player.openInventory(MenuGui.GUI(player));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    @CommandPermission(Permissions.COMMAND_ADMIN)
    @CommandMethod("menu open <name> <open>")
    @CommandDescription("Deze command geeft je info over de plugin")
    private void commandOpen(final Player player, final CommandSender sender, final @Argument("name") String name, final @Argument("open") String open){
        if (!sender.hasPermission("crazycraft.open")){
            String commandNull = ConfigManager.getData().getString("Message.commandOpen");
            player.sendMessage(chat(commandNull));
        } else {
            if(open != null){
                ConfigManager.getMenu().set("item." + name + ".open", open);
                // save menu
                ConfigManager.menuSave();
                String commandSave = ConfigManager.getData().getString("Message.commandSave");
                player.sendMessage(chat(commandSave));

                TextComponent msg = new TextComponent(chat("&6attraction: &5" + name + " has been " + open));
                msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cpark tp " + name));
                Bukkit.getServer().spigot().broadcast(msg);
            } else {
                String commandNull = ConfigManager.getData().getString("Message.commandOpen");
                player.sendMessage(chat(commandNull));
            }
        }
    }
    @CommandPermission(Permissions.COMMAND_ADMIN)
    @CommandMethod("menu add <location>")
    @CommandDescription("Deze command geeft je info over de plugin")
    private void commandAdd(final Player player, final CommandSender sender, final @Argument("location") String location){

    }

    @CommandMethod("cpark tp <location>")
    @CommandDescription("Hier kun je tp naar je atractie")
    private void commandLocation(final Player player, final CommandSender sender, final @Argument("location") String location){
        if (!sender.hasPermission("crazycraft.tp")){
            String commandNull = ConfigManager.getData().getString("Message.commandOpen");
            player.sendMessage(chat(commandNull));
        } else {
            Set<String> keyB = ConfigManager.getMenu().getConfigurationSection("item").getKeys(false);
            for (String key : keyB) {
                int x = 0;
                int y = 0;
                int z = 0;
                String name = null;
                name = ConfigManager.getMenu().getString("item." + key + ".name");
                if (location.equals(name)) {
                    x = ConfigManager.getMenu().getInt("item." + key + ".x");
                    y = ConfigManager.getMenu().getInt("item." + key + ".y");
                    z = ConfigManager.getMenu().getInt("item." + key + ".z");
                    System.out.println(name);
                    Location newLocation = new Location(player.getWorld(), x, y, z);
                    player.teleport(newLocation);
                }
            }
        }
    }

    @CommandMethod("cpark set spawn")
    @CommandDescription("set spawn world")
    private void commandSetSpawn(Player player, final CommandSender sender){
        setSpawn(player);
        player.sendMessage("Spawn is aangepast");
    }

    @CommandMethod("cpark spawn")
    @CommandDescription("set spawn world")
    private void commandSpawn(Player player, final CommandSender sender){
        teleportSpawn(player);
        player.sendMessage("Je bent nu in de Spawn!!");
    }
    @CommandMethod("cpark addlight <name> <light>")
    @CommandDescription("light add")
    private void commandLightadd(Player player, final CommandSender sender, final @Argument("name") String name, final @Argument("light") int light){

        int x = 0;
        int y = 0;
        int z = 0;
        World world = null;
        String lights = ConfigManager.getData().getString("light." + name);

        if (sender instanceof Player) {
            Player p = (Player) sender;
            Location l = p.getLocation();
            x = (int) l.getX();
            y = (int) l.getY();
            z = (int) l.getZ();

            world = p.getLocation().getWorld();
            assert lights != null;
            Set<String> keyB = ConfigManager.getData().getConfigurationSection("light" + name).getKeys(false);
            for (String idLight : keyB) {
                ConfigManager.getData().set("Light." + idLight + ".x", x);
                ConfigManager.getData().set("light." + idLight + ".y", y);
                ConfigManager.getData().set("light." + idLight + ".z", z);
                ConfigManager.getData().set("light." + idLight + ".world", world);
                Location location = new Location(world, x, y, z);
                changeLights(light, 1, 1, String.valueOf(keyB),  location);
            }
        }
    }
}
