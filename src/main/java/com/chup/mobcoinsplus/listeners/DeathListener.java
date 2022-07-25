package com.chup.mobcoinsplus.listeners;

import com.chup.mobcoinsplus.Config;
import com.chup.mobcoinsplus.Main;
import com.chup.mobcoinsplus.extras.ChatUtil;
import com.chup.mobcoinsplus.extras.Extras;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {
    private final Main plugin;

    public DeathListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity().getPlayer();

        double percent = Config.getPercentLossOnDeath();

        for (String key : plugin.getConfig().getConfigurationSection("groups").getKeys(false)) {
            if (player.hasPermission("mobcoinsplus." + key)) {
                percent = plugin.getConfig().getDouble("groups." + key + ".percent_loss_on_death");
            }
        }

        int amount = Extras.getCoins(player.getUniqueId());
        if (percent > 0) {
            if (amount > 0) {
                double percentLoss = Math.round(amount * percent);
                int newAmount = amount - (int) percentLoss;
                Extras.setCoins(player, newAmount);
                String message = plugin.getMessages().getString("lost-coins")
                        .replace("{percent}", Integer.toString((int) (percent * 100)))
                        .replace("{amount}", Integer.toString((int) percentLoss));
                ChatUtil.sendPrefixedMessage(player,message);
            } else {
                String message = plugin.getMessages().getString("lost-coins")
                        .replace("{percent}", Integer.toString((int) (percent * 100)))
                        .replace("{amount}", Integer.toString(0));
                ChatUtil.sendPrefixedMessage(player,message);
            }
        }
    }
}
