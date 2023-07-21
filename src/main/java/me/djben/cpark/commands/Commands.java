package me.djben.cpark.commands;

import me.djben.cpark.Cpark;
import me.djben.cpark.commands.cloud.CloudHandler;
import me.djben.cpark.manager.ConfigManager;
import static me.djben.cpark.utils.Chat.chat;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import com.bergerkiller.bukkit.common.cloud.CloudSimpleHandler;

import java.util.Collections;


public class Commands {
    private final CloudHandler cloud = new CloudHandler();
    private final MenuCommands commands_menu = new MenuCommands();
    private final FlyCommands commands_fly = new FlyCommands();
    private final AFKCommands commands_afk = new AFKCommands();
    public CloudHandler getHandler() {
        return cloud;
    }
    public void enable(Cpark plugin) {
        cloud.enable(plugin);
        // Register all the commands
        cloud.annotations(commands_menu);
        cloud.annotations(commands_fly);
        cloud.annotations(commands_afk);
        cloud.annotations(this);

        cloud.helpCommand(Collections.singletonList("menu"), "Shows help for commands that menu");
        cloud.helpCommand(Collections.singletonList("cpark"), "Shows help for commands that cpark");

    }

    @CommandMethod("cpark")
    @CommandDescription("Deze command geeft je info over de plugin")
    private void commandInfo(final Player player, final CommandSender sender, final Cpark plugin) {
        sender.sendMessage(ChatColor.BLUE + "cpark version 1.5 beta");
    }
    @CommandMethod("cpark reload")
    @CommandDescription("Deze commmand reload de config")
    private void commandReload(final Player player, final CommandSender sender) {
        if (!sender.hasPermission("crazycraft.reload")){
            String commandNull = ConfigManager.getData().getString("Message.commandOpen");
            player.sendMessage(chat(commandNull));
        } else {
            // reload the config and menu
            ConfigManager.reloadConfig();
            ConfigManager.reloadMenu();
            sender.sendMessage(chat("&3The config is reload"));
        }
    }
}