package com.chup.mobcoinsplus.extras;


import com.chup.mobcoinsplus.Config;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ChatUtil {

    @Contract("_ -> new")
    public static @NotNull String color(final String message) {
        return ChatColor.translateAlternateColorCodes('&',message);
    }

    public static void sendPrefixedMessage(final CommandSender sender, final String message) {
        sender.sendMessage(color(Config.getPluginPrefix() + message));
    }

}
