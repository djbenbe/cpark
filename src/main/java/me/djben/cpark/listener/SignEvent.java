package me.djben.cpark.listener;

import me.djben.cpark.utils.Chat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignEvent implements Listener {

    @EventHandler(priority= EventPriority.NORMAL, ignoreCancelled=true)
    public void onSignEvent(SignChangeEvent info) {
        String[] lineArray = info.getLines();
        int lineArrayLength = lineArray.length;

        for(int i = 0; i < lineArrayLength; i++) {
            String oldLine = lineArray[i];
            String newLine = Chat.chat(oldLine);
            info.setLine(i, newLine);
        }
    }
}