package me.djben.cpark.utils;

import com.bergerkiller.bukkit.tc.Station;
import com.bergerkiller.bukkit.tc.attachments.animation.AnimationOptions;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.google.common.collect.Maps;
import me.djben.cpark.Cpark;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.Map;

import static me.djben.cpark.utils.Chat.chat;

public class MainCoaster {
    private static final Map<String, MainCoaster> ROLLERCOASTER = Maps.newHashMap();
    private SignActionEvent rideInStation;
    private Block gatesActivator;
    private int maxCountdown = -1;
    private int countdown = -1;
    private boolean countdownRunning;
    private BlockFace direction;
    private int currentCountDownState;

    private MainCoaster(SignActionEvent e) {
        this.currentCountDownState = this.maxCountdown;
        ROLLERCOASTER.put(e.getLine(3), this);
    }

    public static MainCoaster getMainCoaster(SignActionEvent e) {
        return ROLLERCOASTER.containsKey(e.getLine(3)) ? (MainCoaster)ROLLERCOASTER.get(e.getLine(3)) : new MainCoaster(e);
    }

    public void setGates(boolean gates) {
        this.gatesActivator.setType(gates ? Material.REDSTONE_BLOCK : Material.DIAMOND_BLOCK);
    }
    public void countdownAnimate(SignActionEvent event) {
        if (!this.countdownRunning) {
            this.countdownRunning = true;
            this.countdown = Bukkit.getScheduler().scheduleSyncRepeatingTask(Cpark.getInstance(), () -> {
                --this.currentCountDownState;
                if (this.currentCountDownState < 1) {
                    this.rideInStation.getGroup().forEach((minecartMember) -> {
                        (minecartMember.getEntity()).getPlayerPassengers().forEach((player) -> {
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, (new ComponentBuilder(chat("The attraction starts &1now"))).create());
                        });
                    });
                    AnimationOptions options = new AnimationOptions();
                    options.loadFromSign(event);
                    for (MinecartMember<?> mm : event.getMembers()) {
                        mm.playNamedAnimation(options);
                    }
                    this.cancelCountdown();
                } else {
                    this.rideInStation.getGroup().forEach((minecartMember) -> {
                        (minecartMember.getEntity()).getPlayerPassengers().forEach((player) -> {
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0F, 1.0F);
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, (new ComponentBuilder(chat("The attraction starts in &a" + (this.currentCountDownState == 1 ? "one &fsecond" : this.currentCountDownState + "&f seconds")))).create());
                        });
                    });
                }

            }, 0L, 20L);
        }

    }
    public void countdown() {
        if (!this.countdownRunning) {
            this.countdownRunning = true;
            this.countdown = Bukkit.getScheduler().scheduleSyncRepeatingTask(Cpark.getInstance(), () -> {
                --this.currentCountDownState;
                if (this.currentCountDownState < 1) {
                    this.rideInStation.getGroup().forEach((minecartMember) -> {
                        (minecartMember.getEntity()).getPlayerPassengers().forEach((player) -> {
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, (new ComponentBuilder(chat("The attraction starts &1now"))).create());
                        });
                    });
                    this.launch();
                    this.cancelCountdown();
                } else {
                    this.rideInStation.getGroup().forEach((minecartMember) -> {
                        (minecartMember.getEntity()).getPlayerPassengers().forEach((player) -> {
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0F, 1.0F);
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, (new ComponentBuilder(chat("The attraction starts in &a" + (this.currentCountDownState == 1 ? "one &fsecond" : this.currentCountDownState + "&f seconds")))).create());
                        });
                    });
                }

            }, 0L, 20L);
        }

    }

    public void cancelCountdown() {
        Bukkit.getScheduler().cancelTask(this.countdown);
        this.currentCountDownState = this.maxCountdown;
        this.countdown = -1;
        this.countdownRunning = false;
    }

    public void launch() {
        this.rideInStation.getGroup().playNamedAnimation("on");
        this.rideInStation.getGroup().getProperties().setPlayersEnter(false);
        this.rideInStation.getGroup().getProperties().setPlayersExit(false);
        this.setGates(true);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Cpark.getInstance(), () -> {
            Station station = new Station(this.rideInStation);
            station.getLaunchConfig().setDistance(5.0);
            station.launchTo(this.direction);
        }, 100L);
    }

    public SignActionEvent getRideInStation() {
        return this.rideInStation;
    }

    public Block getGatesActivator() {
        return this.gatesActivator;
    }

    public int getMaxCountdown() {
        return this.maxCountdown;
    }

    public int getCountdown() {
        return this.countdown;
    }

    public boolean isCountdownRunning() {
        return this.countdownRunning;
    }

    public BlockFace getDirection() {
        return this.direction;
    }

    public int getCurrentCountDownState() {
        return this.currentCountDownState;
    }

    public void setRideInStation(SignActionEvent rideInStation) {
        this.rideInStation = rideInStation;
    }

    public void setGatesActivator(Block gatesActivator) {
        this.gatesActivator = gatesActivator;
    }

    public void setMaxCountdown(int maxCountdown) {
        this.maxCountdown = maxCountdown;
    }

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }

    public void setCountdownRunning(boolean countdownRunning) {
        this.countdownRunning = countdownRunning;
    }

    public void setDirection(BlockFace direction) {
        this.direction = direction;
    }

    public void setCurrentCountDownState(int currentCountDownState) {
        this.currentCountDownState = currentCountDownState;
    }

}
