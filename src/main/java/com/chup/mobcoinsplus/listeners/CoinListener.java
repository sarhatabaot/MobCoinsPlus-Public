package com.chup.mobcoinsplus.listeners;

import com.chup.mobcoinsplus.Config;
import com.chup.mobcoinsplus.Main;
import com.chup.mobcoinsplus.extras.ChatUtil;
import com.chup.mobcoinsplus.extras.SLAPI;
import com.cryptomorin.xseries.XSound;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.List;
import java.util.Random;

public class CoinListener implements Listener {

    String prefix = ChatColor.translateAlternateColorCodes('&', Config.getPluginPrefix());

    private final Main plugin;

    public CoinListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        List<String> worlds = plugin.getConfig().getStringList("disabled-worlds");
        String currency = Config.getCurrencyName();
        Entity dead = e.getEntity();
        if (dead.getLastDamageCause() == null) {
            return;
        }

        EntityDamageEvent d = dead.getLastDamageCause();
        if (!(d instanceof EntityDamageByEntityEvent)) {
            return;
        }

        if (((EntityDamageByEntityEvent) d).getDamager() instanceof Player) {
            Player player = (Player) ((EntityDamageByEntityEvent) d).getDamager();
            if (!worlds.contains(player.getWorld().getName())) {
                if (!Main.getPoints().containsKey(player.getUniqueId())) {
                    Main.getPoints().put(player.getUniqueId(), 0);
                }
                Random ran = plugin.getRandom();
                int choice = ran.nextInt(100) + 1;
                int chance = Config.getChanceMob(dead.getType().toString());
                int amount = Config.getAmountMob(dead.getType().toString());
                int finalAmount = amount;

                for (String key : plugin.getConfig().getConfigurationSection("groups").getKeys(false)) {
                    if (player.hasPermission("mobcoinsplus." + key)) {
                        finalAmount = amount * plugin.getConfig().getInt("groups." + key + ".coin_multiplier");
                    }
                }

                if (choice <= chance && choice > 0) {
                    Main.getPoints().put(player.getUniqueId(), Main.getPoints().get(player.getUniqueId()) + finalAmount);
                    if (Config.getMessageStatus()) {
                        String message = plugin.getMessages().getString("coins-collected");
                        message = message.replace("{amount}", Integer.toString(finalAmount));
                        message = message.replace("{currency}", currency);
                        ChatUtil.sendPrefixedMessage(player, message);
                    }
                    if (Config.getSoundStatus()) {
                        player.playSound(player.getLocation(), XSound.ENTITY_EXPERIENCE_ORB_PICKUP.parseSound(), 1.0F, 1.0F);
                    }
                    try {
                        SLAPI.save(Main.getPoints(), "./plugins/MobCoinsPlus/data/coins.bin");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } else if (((EntityDamageByEntityEvent) d).getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) ((EntityDamageByEntityEvent) d).getDamager();
            if (arrow.getShooter() instanceof Player) {
                Player player = (Player) arrow.getShooter();
                if (!worlds.contains(player.getWorld().getName())) {
                    if (!Main.getPoints().containsKey(player.getUniqueId())) {
                        Main.getPoints().put(player.getUniqueId(), 0);
                    }
                    Random ran = plugin.getRandom();
                    int choice = ran.nextInt(100) + 1;
                    int chance = Config.getChanceMob(dead.getType().toString());
                    int amount = Config.getAmountMob(dead.getType().toString());
                    int finalAmount = amount;

                    for (String key : plugin.getConfig().getConfigurationSection("groups").getKeys(false)) {
                        if (player.hasPermission("mobcoinsplus." + key)) {
                            finalAmount = amount * plugin.getConfig().getInt("groups." + key + ".coin_multiplier");
                        }
                    }

                    if (choice <= chance && choice > 0) {
                        Main.getPoints().put(player.getUniqueId(), Main.getPoints().get(player.getUniqueId()) + finalAmount);
                        if (Config.getMessageStatus()) {
                            String message = plugin.getMessages().getString("coins-collected")
                                    .replace("{amount}", Integer.toString(finalAmount))
                                    .replace("{currency}", currency);
                            ChatUtil.sendPrefixedMessage(player, message);
                        }
                        if (Config.getSoundStatus()) {
                            player.playSound(player.getLocation(), XSound.ENTITY_EXPERIENCE_ORB_PICKUP.parseSound(), 1.0F, 1.0F);
                        }
                        try {
                            SLAPI.save(Main.getPoints(), "./plugins/MobCoinsPlus/data/coins.bin");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }


    }
}