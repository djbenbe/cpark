package me.djben.cpark.utils;

import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.components.ActionTrackerGroup;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;

public class Property {

    private final SignActionEvent info;
    private final long delay;
    public Property(SignActionEvent info) {
        this(info, PropertyConfig.fromSign(info));
    }

    public Property(SignActionEvent infoE, PropertyConfig config) {
        info = infoE;
        delay = config.getDelay();
    }
    /**
     * Gets the Minecart Group initiating this station
     *
     * @return group
     */
    public MinecartGroup getGroup() {
        return this.info.getGroup();
    }

    public void waitTrainTime(long delay) {
        MinecartGroup group = this.getGroup();
        ActionTrackerGroup actions = info.getGroup().getActions();
        if (TCConfig.playHissWhenStopAtStation) {
            actions.addActionSizzle().addTag(this.getTag());
        }
        if (TCConfig.refillAtStations) {
            actions.addActionRefill().addTag(this.getTag());
        }
        setLevers(true);
        if (delay == Long.MAX_VALUE) {
            actions.addActionWaitForever().addTag(this.getTag());
        } else if (delay > 0) {
            actions.addActionWait(delay).addTag(this.getTag());
            setLevers(false);
        }
    }

    public void setLevers(boolean down) {
        info.getGroup().getActions().addActionSetLevers(info.getAttachedBlock(), down).addTag(this.getTag());
    }
    /**
     * Gets a tag unique to this station's location.
     * Is applied to all actions executed by this station
     *
     * @return tag
     */
    public String getTag() {
        return StringUtil.blockToString(this.info.getBlock());
    }

    /**
     * Gets whether this station has a delay set
     *
     * @return True if a delay is set, False if not
     */
    public boolean hasDelay() {
        return delay > 0;
    }

    /**
     * Gets the delay between action and launch (in milliseconds)
     *
     * @return action delay
     */
    public long getDelay() {
        return delay;
    }


    public static class PropertyConfig {
        private long _delay = 0;

        public long getDelay() {
            return _delay;
        }
        public void setDelay(long delay) {
            _delay = delay;
        }
        public static PropertyConfig fromSign(SignActionEvent info) {
            PropertyConfig config = new PropertyConfig();

            config.setDelay(ParseUtil.parseTime(info.getLine(2)));
            return config;
        }
    }

}