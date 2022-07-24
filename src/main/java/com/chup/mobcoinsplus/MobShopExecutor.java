package com.chup.mobcoinsplus;

import com.chup.mobcoinsplus.guis.MobShopGUI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MobShopExecutor implements CommandExecutor {

    String prefix = ChatColor.translateAlternateColorCodes('&', Config.getPluginPrefix());

    private final Main plugin;

    public MobShopExecutor(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            plugin.getLogger().warning("This command can't be run through console.");
            return false;
        }

        Player player = (Player) sender;
        if (!Config.getPermissionStatus()) {
            new MobShopGUI(player, 1);
            return true;
        }

        if (player.hasPermission("mobcoinsplus.mobshop")) {
            new MobShopGUI(player, 1);
            return true;
        }

        String message = plugin.getMessages().getString("no-permission");
        player.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', message));
        return true;
    }
}