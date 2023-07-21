package me.djben.cpark;

import com.bergerkiller.bukkit.common.PluginBase;
import com.bergerkiller.bukkit.tc.signactions.SignAction;
import me.djben.cpark.commands.Commands;
import me.djben.cpark.listener.Events;
import me.djben.cpark.listener.JoinEvent;
import me.djben.cpark.listener.SignEvent;
import me.djben.cpark.listener.Spawn;
import me.djben.cpark.manager.AFKManager;
import me.djben.cpark.manager.ConfigManager;
import me.djben.cpark.signaction.ParkTrain;
import me.djben.cpark.signaction.StationTime;
import me.djben.cpark.listener.ChatFormat;
import me.djben.cpark.utils.MenuGui;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Calendar;
import java.util.TimeZone;

public class Cpark extends PluginBase {
    public static ConfigManager data;
    public static Cpark plugin;
    private Commands commands;
    private static World world;
    public static Cpark getInstance() {
        return plugin;
    }


    @Override
    public void onLoad() {
        commands = new Commands();
        plugin = this;
    }
    @Override
    public int getMinimumLibVersion() {
        return 0;
    }
    @Override
    public void enable() {
        plugin = this;
        data = new ConfigManager(this);
        commands.enable(this);
        // Update every minute
        long interval = ConfigManager.getData().getLong("World.UpdateInterval");
        Boolean worldTime = ConfigManager.getData().getBoolean("World.Time");
        ChatFormat.refreshVault();
        world = getServer().getWorld("world");
        SignAction.register(new ParkTrain());
        SignAction.register(new StationTime());
        getServer().getPluginManager().registerEvents(new ChatFormat(), this);
        getServer().getPluginManager().registerEvents(new Spawn(), this);
        getServer().getPluginManager().registerEvents(new JoinEvent(), this);
        getServer().getPluginManager().registerEvents(new Events(), this);
        getServer().getPluginManager().registerEvents(new SignEvent(), this);
        MenuGui.initialize();

        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, () -> {
            // Write current enabled/disabled values to config
            AFKManager.checkAllPlayersAFKStatus();
            setTime("MET");
        }, 0L, interval);
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, worldTime);
        getLogger().info("Main plugin Has been Enabled.");
    }
    @Override
    public void disable() {
        SignAction.unregister(new StationTime());
        SignAction.unregister(new ParkTrain());
        getLogger().info("Main plugin Has been Disabled.");
    }
    private static void setTime(String timezone) {
        // Ticks = (Hours * 1000) - 6000
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(timezone)); // Get the time instance
        long time = (1000 * cal.get(Calendar.HOUR_OF_DAY)) + (16 * cal.get(Calendar.MINUTE)) - 6000;
        world.setTime(time);
    }
    @Override
    public boolean command(CommandSender commandSender, String s, String[] strings) {
        return false;
    }
}
