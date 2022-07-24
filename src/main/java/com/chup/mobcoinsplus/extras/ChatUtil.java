package com.chup.mobcoinsplus.extras;


import org.bukkit.ChatColor;

public class ChatUtil {

    public static String color(final String message) {
        return ChatColor.translateAlternateColorCodes('&',message);
    }
}
