package me.djben.cpark.signaction;

import com.bergerkiller.bukkit.tc.Station;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent;
import com.bergerkiller.bukkit.tc.signactions.SignAction;
import com.bergerkiller.bukkit.tc.signactions.SignActionType;
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions;
import me.djben.cpark.Cpark;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;

import java.util.Locale;

public class ParkTrain extends SignAction {
    public boolean match(SignActionEvent event) {
        return event.isType("parktrain");
    }

    public void execute(SignActionEvent event) {
        int[] countdown = new int[1];
        if (event.isAction(SignActionType.GROUP_ENTER)) {
            event.getGroup().getActions().launchReset();
            Station station = new Station(event);
            station.centerTrain();
            int[] currentCountDownState = new int[]{Integer.parseInt(event.getLine(2))};
            Bukkit.getScheduler().scheduleSyncDelayedTask(Cpark.getInstance(), () -> {
                event.getGroup().getProperties().setPlayersEnter(true);
                event.getGroup().getProperties().setPlayersExit(true);
                event.getGroup().playNamedAnimation("off");
                countdown[0] = Bukkit.getScheduler().scheduleSyncRepeatingTask(Cpark.getInstance(), () -> {
                    int var10003 = currentCountDownState[0];
                    int var10000 = currentCountDownState[0];
                    currentCountDownState[0] = var10003 - 1;
                    if (currentCountDownState[0] < 1) {
                        event.getGroup().forEach((minecartMember) -> {
                            (minecartMember.getEntity()).getPlayerPassengers().forEach((player) -> {
                                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, (new ComponentBuilder("The attraction starts §anow")).create());
                            });
                        });
                        Bukkit.getScheduler().cancelTask(countdown[0]);
                        event.getGroup().getProperties().setPlayersEnter(false);
                        event.getGroup().getProperties().setPlayersExit(false);
                        event.getGroup().playNamedAnimation("off");
                        Bukkit.getScheduler().scheduleSyncDelayedTask(Cpark.getInstance(), () -> {
                            station.getLaunchConfig().setDistance(3.0);
                            station.launchTo(BlockFace.valueOf(event.getLine(3).toUpperCase(Locale.ROOT)));
                        }, 100L);
                    } else {
                        event.getGroup().forEach((minecartMember) -> {
                            (minecartMember.getEntity()).getPlayerPassengers().forEach((player) -> {
                                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0F, 1.0F);
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, (new ComponentBuilder("The attraction starts in §a" + (currentCountDownState[0] == 1 ? "one $fsecond" :  currentCountDownState[0] + "§fseconds"))).create());

                            });
                        });
                    }
                }, 0L, 20L);
            }, 100L);
        }
    }
    @Override
    public boolean build(SignChangeActionEvent event) {
        return SignBuildOptions.create()
                .setName(event.isCartSign() ? "cart holdandrelease" : "train holdandrelease")
                .setDescription("holds the train for x seconds then leaves the train free to handle by gravity")
                .handle(event.getPlayer());
    }
}
