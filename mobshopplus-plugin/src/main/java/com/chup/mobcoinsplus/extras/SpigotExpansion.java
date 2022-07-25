package com.chup.mobcoinsplus.extras;

import com.chup.mobcoinsplus.Main;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.UUID;

public class SpigotExpansion extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "mobcoinsplus";
    }

    @Override
    public @NotNull String getAuthor() {
        return "GupAChup";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.6.3";
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) {
            return "";
        }

        if(params.equals("playerbalance")) {
            if(Main.getPoints().containsKey(player.getUniqueId())) {
                return String.valueOf(Main.getPoints().get(player.getUniqueId()));
            }
            return String.valueOf(0);
        }
        if(params.equals("playerbalance_formatted")) {
            DecimalFormat formatter = new DecimalFormat("###,###,###,###,###,###,###,###");
            if(Main.getPoints().containsKey(player.getUniqueId())) {
                return formatter.format(Main.getPoints().get(player.getUniqueId()));
            }

            return String.valueOf(0);
        }
        if(params.contains("coinstop_player_")) {
            Map<UUID, Integer> top = CoinsTop.sortByValue(Main.getPoints());
            int cycle = 0;
            int position = Integer.parseInt(ChatColor.stripColor(params).replaceAll("[^0-9]", ""));
            for (Map.Entry<UUID, Integer> en : top.entrySet()) {
                String name = Bukkit.getServer().getOfflinePlayer(en.getKey()).getName();
                cycle = cycle + 1;
                if (cycle == position) {
                    return name;
                }
            }
        }

        if(params.contains("coinstop_amount_")) {
            Map<UUID, Integer> top = CoinsTop.sortByValue(Main.getPoints());
            DecimalFormat formatter = new DecimalFormat("###,###,###,###,###,###,###,###");
            int cycle = 0;
            int position = Integer.parseInt(ChatColor.stripColor(params).replaceAll("[^0-9]", ""));
            for (Map.Entry<UUID, Integer> en : top.entrySet()) {
                cycle = cycle + 1;
                if (cycle == position) {
                    return formatter.format(en.getValue());
                }
            }
        }
        return null;
    }
}