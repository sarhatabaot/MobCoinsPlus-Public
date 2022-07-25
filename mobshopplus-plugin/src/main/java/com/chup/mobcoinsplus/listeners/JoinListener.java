package com.chup.mobcoinsplus.listeners;

import com.chup.mobcoinsplus.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private final Main plugin;

    public JoinListener(Main main) {
        this.plugin = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (!Main.getPoints().containsKey(player.getUniqueId())) {
            int amount = 0;
            if (plugin.getConfig().contains("starting-amount")) {
                amount = plugin.getConfig().getInt("starting-amount");
            }
            Main.getPoints().put(player.getUniqueId(), amount);
        }
    }
}
