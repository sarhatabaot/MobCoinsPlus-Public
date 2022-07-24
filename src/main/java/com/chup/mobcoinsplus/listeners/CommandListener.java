package com.chup.mobcoinsplus.listeners;

import com.chup.mobcoinsplus.Config;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;

public class CommandListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage();
        String[] sMsg = message.split(" ");
        String mainCmd = Config.getMainCommand();
        String mobShopCmd = Config.getMobShopCommand();
        if (sMsg[0].equalsIgnoreCase("/" + mainCmd)) {
            StringBuilder messageJ = new StringBuilder("/mobcoins");
            for (int i = 1; i < sMsg.length; i++) {
                messageJ.append(" ").append(sMsg[i]);
            }
            event.setMessage(messageJ.toString());
            return;
        }

        if (sMsg[0].equalsIgnoreCase("/" + mobShopCmd)) {
            StringBuilder messageJ = new StringBuilder("/mobshop");
            for (int i = 1; i < sMsg.length; i++) {
                messageJ.append(" ").append(sMsg[i]);
            }
            event.setMessage(messageJ.toString());
        }
    }
}
