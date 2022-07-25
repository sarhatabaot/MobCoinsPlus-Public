package com.chup.mobcoinsplus.extras;

import com.chup.mobcoinsplus.Main;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class Extras {

    public static void giveCoins(@NotNull Player player, int amount) {
        if (!Main.getPoints().containsKey(player.getUniqueId())) {
            Main.getPoints().put(player.getUniqueId(), amount);
        } else {
            int playerPoints = Main.getPoints().get(player.getUniqueId());
            Main.getPoints().put(player.getUniqueId(), playerPoints + amount);
        }

    }

    public static void removeCoins(@NotNull Player player, int amount) {
        if (!Main.getPoints().containsKey(player.getUniqueId())) {
            Main.getPoints().put(player.getUniqueId(), -amount);
        } else {
            int playerPoints = Main.getPoints().get(player.getUniqueId());
            Main.getPoints().put(player.getUniqueId(), playerPoints - amount);
        }

    }

    public static void setCoins(@NotNull Player player, int amount) {
        Main.getPoints().put(player.getUniqueId(), amount);
    }

    public static Integer getCoins(UUID uuid) {
        if (!Main.getPoints().containsKey(uuid)) {
            return 0;
        }
        return Main.getPoints().get(uuid);
    }

    public static @NotNull ItemStack duplicateItem(@NotNull Player player) {
        return player.getInventory().getItemInMainHand().clone();
    }

    public static ItemStack getColor(@NotNull String color) {
        switch (color.toLowerCase()) {
            case "black":
                return XMaterial.BLACK_STAINED_GLASS_PANE.parseItem();
            case "red":
                return XMaterial.RED_STAINED_GLASS_PANE.parseItem();
            case "green":
                return XMaterial.GREEN_STAINED_GLASS_PANE.parseItem();
            case "brown":
                return XMaterial.BROWN_STAINED_GLASS_PANE.parseItem();
            case "blue":
                return XMaterial.BLUE_STAINED_GLASS_PANE.parseItem();
            case "purple":
                return XMaterial.PURPLE_STAINED_GLASS_PANE.parseItem();
            case "light gray":
            case "light_gray":
            case "lightgray":
                return XMaterial.LIGHT_GRAY_STAINED_GLASS_PANE.parseItem();
            case "gray":
                return XMaterial.GRAY_STAINED_GLASS_PANE.parseItem();
            case "pink":
                return XMaterial.PINK_STAINED_GLASS_PANE.parseItem();
            case "lime":
                return XMaterial.LIME_STAINED_GLASS_PANE.parseItem();
            case "yellow":
                return XMaterial.YELLOW_STAINED_GLASS_PANE.parseItem();
            case "light blue":
            case "light_blue":
            case "lightblue":
                return XMaterial.LIGHT_BLUE_STAINED_GLASS_PANE.parseItem();
            case "magenta":
                return XMaterial.MAGENTA_STAINED_GLASS_PANE.parseItem();
            case "orange":
                return XMaterial.ORANGE_STAINED_GLASS_PANE.parseItem();
            case "white":
                return XMaterial.WHITE_STAINED_GLASS_PANE.parseItem();
            default:
                return XMaterial.GLASS_PANE.parseItem();
        }
    }

}
