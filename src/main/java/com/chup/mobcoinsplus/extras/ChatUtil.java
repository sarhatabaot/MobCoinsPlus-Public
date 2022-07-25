package com.chup.mobcoinsplus.extras;


import org.bukkit.ChatColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ChatUtil {

    @Contract("_ -> new")
    public static @NotNull String color(final String message) {
        return ChatColor.translateAlternateColorCodes('&',message);
    }
}
