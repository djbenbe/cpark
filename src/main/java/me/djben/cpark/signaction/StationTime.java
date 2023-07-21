package me.djben.cpark.signaction;

import com.bergerkiller.bukkit.tc.Station;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent;
import com.bergerkiller.bukkit.tc.signactions.SignAction;
import com.bergerkiller.bukkit.tc.signactions.SignActionType;
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions;
import me.djben.cpark.Cpark;
import me.djben.cpark.utils.MainCoaster;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

import java.util.Locale;

public class StationTime extends SignAction {

    @Override
    public boolean match(SignActionEvent info) {
        return info.isType("mainstation");
    }

    @Override
    public void execute(SignActionEvent event) {
        MainCoaster rollerCoaster = MainCoaster.getMainCoaster(event);
        rollerCoaster.setRideInStation(event);
        if (event.isAction(SignActionType.REDSTONE_CHANGE)) {
            return;
        }
        MinecartGroup group = event.getGroup();
        Station station = new Station(event);
        if (event.isAction(SignActionType.GROUP_ENTER)) {
            group.getActions().launchReset();
            group.getProperties().setSlowingDown(false);
            station.centerTrain();
            String[] params = event.getLine(2).split(" ");
            String[] params2 = event.getLine(3).split(" ");
            rollerCoaster.setGatesActivator((new Location(event.getWorld(), (double)Integer.parseInt(params[0]), (double)Integer.parseInt(params[1]), (double)Integer.parseInt(params[2]))).getBlock());
            rollerCoaster.setMaxCountdown(Integer.parseInt(params2[0]));
            rollerCoaster.setCurrentCountDownState(rollerCoaster.getMaxCountdown());
            rollerCoaster.setDirection(BlockFace.valueOf(params2[1].toUpperCase(Locale.ROOT)));
            Bukkit.getScheduler().scheduleSyncDelayedTask(Cpark.getInstance(), () -> {
                group.eject();
                group.getProperties().setPlayersEnter(true);
                group.getProperties().setPlayersExit(true);
                rollerCoaster.setGates(false);
                event.getGroup().playNamedAnimation("on");
            }, 100L);
        } else if (event.isAction(SignActionType.GROUP_UPDATE)) {
            if (group.hasPassenger()) {
                if (group.getProperties().getPlayersEnter()) {
                    rollerCoaster.countdown();
                } else {
                    rollerCoaster.cancelCountdown();
                }
            } else {
                rollerCoaster.cancelCountdown();
                group.getActions().launchReset();
                station.centerTrain();
            }
        }
    }
    @Override
    public boolean build(SignChangeActionEvent event) {
        return SignBuildOptions.create()
                .setName(event.isCartSign() ? "cart mainstation" : "train mainstation")
                .setDescription("Station timer is a reddy")
                .handle(event.getPlayer());
    }

    @Override
    public boolean isRailSwitcher(SignActionEvent info) {
        return true;
    }

    @Override
    public boolean overrideFacing() {
        return true;
    }
}