package me.djben.cpark.utils;

import me.djben.cpark.Cpark;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

public class CountdownTimer implements Runnable {

    // Main class for bukkit scheduling
    final private JavaPlugin plugin;
    // Our scheduled task's assigned id, needed for canceling
    private static Integer assignedTaskId;

    // Seconds and shiz
    final private int seconds;
    private int secondsLeft;
    public static boolean hasTime;
    // Actions to perform while counting down, before and after
    final private Consumer<CountdownTimer> everySecond;
    final private Runnable beforeTimer;

    // Construct a timer, you could create multiple so for example if
    // you do not want these "actions"
    public CountdownTimer(Cpark plugin, int seconds,
                          Runnable beforeTimer,
                          Consumer<CountdownTimer> everySecond, boolean hasTime1) {
        // Initializing fields
        this.plugin = plugin;

        this.seconds = seconds;
        this.secondsLeft = seconds;

        this.beforeTimer = beforeTimer;
        this.everySecond = everySecond;
        hasTime = hasTime1;
    }

    /**
     * Gets the total seconds this timer was set to run for
     *
     * @return Total seconds timer should run
     */
    public int getTotalSeconds() {
        return seconds;
    }

    /**
     * Gets the seconds left this timer should run
     *
     * @return Seconds left timer should run
     */
    public int getSecondsLeft() {
        return secondsLeft;
    }
    public Integer getAssignedTaskId() {
        return assignedTaskId;
    }

    public static boolean isHasTime() {
        return hasTime;
    }

    public static void setHasTime(boolean hasTime1) {
        hasTime = hasTime1;
    }

    public static void cancelCountdown() {
        Bukkit.getScheduler().cancelTask(assignedTaskId);
        setHasTime(false);
    }
    /**
     * Schedules this instance to "run" every second
     */
    public void scheduleTimer() {
        // Initialize our assigned task's id, for later use so we can cancel
        assignedTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 0L, 20L);
    }
    public boolean isImportantSecond() {
        return ((this.secondsLeft % 15) == 0) || (this.secondsLeft < 11 && this.secondsLeft >= 1);
    }
    /**
     * Runs the timer once, decrements seconds etc...
     * Really wish we could make it protected/private so you couldn't access it
     */
    @Override
    public void run() {
        // Is the timer up?
        if (isHasTime()){
            if (secondsLeft < 0) {
                // Do what was supposed to happen after the timer
                // Cancel timer
                Bukkit.getScheduler().cancelTask(assignedTaskId);
                return;
            }

            // Are we just starting?
            if (secondsLeft == seconds && beforeTimer != null) beforeTimer.run();

            // Do what's supposed to happen every second
            everySecond.accept(this);

            // Decrement the seconds left
            secondsLeft--;
            setHasTime(true);
        } else {
            cancelCountdown();
            setHasTime(false);
        }

    }
}