package com.chup.mobcoinsplus;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.chup.mobcoinsplus.extras.ChatUtil;
import com.chup.mobcoinsplus.extras.CoinsTop;
import com.chup.mobcoinsplus.extras.Extras;
import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.*;

@CommandAlias("mobcoins|mc")
@Description("MobCoins commands.")
public class MobCoinsCommand extends BaseCommand {
    private final Main plugin;

    public MobCoinsCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Subcommand("reload|rl")
    @CommandPermission("mobcoinsplus.admin")
    public void onReload(final @NotNull CommandSender sender) {
        plugin.getConfigManager().reload("messages.yml");
        plugin.getConfigManager().save("messages.yml");
        plugin.reloadConfig();
        String message = ChatUtil.color(plugin.getMessages().getString("reload"));
        ChatUtil.sendPrefixedMessage(sender, message);
    }

    @Subcommand("gui")
    @CommandAlias("mobshop|ms")
    @Description("Opens MobCoins shop.")
    public void onOpenGui(final Player player) {
        if (!Config.getPermissionStatus()) {
            plugin.getGui().openGui(player, 1);
            return;
        }

        if (player.hasPermission("mobcoinsplus.mobshop")) {
            plugin.getGui().openGui(player, 1);
            return;
        }

        String message = plugin.getMessages().getString("no-permission");
        ChatUtil.sendPrefixedMessage(player,message);
    }

    @Subcommand("help")
    @CatchUnknown
    public void onHelp(final CommandSender sender) {
        if (sender.isOp() || sender.hasPermission("mobcoinsplus.admin")) {
            for (final String helpMessage : plugin.getMessages().getStringList("help-admin")) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', helpMessage));
            }
            return;
        }

        if (plugin.getConfig().contains("enable-player-help") && plugin.getConfig().getBoolean("enable-player-help")) {
            for (final String helpMessage : plugin.getMessages().getStringList("help-player")) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', helpMessage));
            }
            return;
        }

        String message = plugin.getMessages().getString("no-permission");
        ChatUtil.sendPrefixedMessage(sender, message);
    }

    @Subcommand("send")
    public void onSend(final Player sender, final OnlinePlayer target, final int amount) {
        if (!Config.getSendMoneyStatus()) {
            onHelp(sender);
            return;
        }

        if (target.getPlayer() == sender) {
            String message = plugin.getMessages().getString("self-send");
            ChatUtil.sendPrefixedMessage(sender,message);
            return;
        }
        if (amount <= 0) {
            //todo enforce at command level
            String message = plugin.getMessages().getString("invalid-amount");
            ChatUtil.sendPrefixedMessage(sender,message);
            return;
        }
        if (Extras.getCoins(sender.getUniqueId()) < amount) {
            String message = plugin.getMessages().getString("negative-coins");
            ChatUtil.sendPrefixedMessage(sender,message);
            return;
        }

        Extras.giveCoins(target.getPlayer(), amount);
        Extras.removeCoins(sender, amount);
        // send message to sender //
        String playerMessage = plugin.getMessages().getString("send-coins")
                .replace("{target}", target.getPlayer().getName())
                .replace("{amount}", Integer.toString(amount));
        ChatUtil.sendPrefixedMessage(sender,playerMessage);
        // send message to recipient //
        String targetMessage = plugin.getMessages().getString("given-coins")
                .replace("{player}", sender.getName())
                .replace("{amount}", Integer.toString(amount));
        ChatUtil.sendPrefixedMessage(target.getPlayer(),targetMessage);
    }

    @CommandPermission("mobcoinsplus.top")
    public void onTop(final CommandSender sender) {
        Map<UUID, Integer> top = CoinsTop.sortByValue(Main.getPoints());
        String decimalFormat = "###,###,###,###,###,###,###,###";
        DecimalFormat formatter = new DecimalFormat(decimalFormat);
        int cycle = 0;
        ChatUtil.sendPrefixedMessage(sender, plugin.getMessages().getString("mobcoins-top-title"));
        for (Map.Entry<UUID, Integer> en : top.entrySet()) {
            String name = Bukkit.getServer().getOfflinePlayer(en.getKey()).getName();
            String format = ChatUtil.color(plugin.getMessages().getString("mobcoins-top-format"));
            format = format.replace("{name}", name);
            format = format.replace("{amount}", formatter.format(en.getValue()));
            format = format.replace("{position}", Integer.toString(cycle + 1));
            sender.sendMessage(format);
            cycle = cycle + 1;
            if (cycle == 10) {
                break;
            }
        }

    }

    @Subcommand("set")
    @CommandPermission("mobcoinsplus.admin")
    public void onSet(final CommandSender sender, final OnlinePlayer target, int newAmount) {
        Extras.setCoins(target.getPlayer(), newAmount);
        String currencyName = Config.getCurrencyName();
        int playerCoins = Extras.getCoins(target.getPlayer().getUniqueId());
        String message = plugin.getMessages().getString("player-new-balance")
                .replace("{total-coins}", Integer.toString(playerCoins))
                .replace("{currency}", currencyName)
                .replace("{player}", target.getPlayer().getName());
        ChatUtil.sendPrefixedMessage(sender,message);
    }

    @Subcommand("additem")
    public void onAddItem(final Player sender, final int price) {
        ItemStack item = Extras.duplicateItem(sender);
        if (item.getType() == XMaterial.AIR.parseMaterial()) {
            String message = plugin.getMessages().getString("invalid-item");
            ChatUtil.sendPrefixedMessage(sender, message);
            return;
        }

        ItemMeta itemMeta = item.getItemMeta();
        String currencySymbol = Config.getCurrencySymbol();
        String priceFormat = ChatUtil.color(plugin.getMessages().getString("price-format"));
        priceFormat = priceFormat.replace("{amount}", Integer.toString(price));
        if (itemMeta.getLore() != null && itemMeta.hasLore()) {
            List<String> lore = new ArrayList<>(itemMeta.getLore());
            lore.add("");
            if (Config.getCurrencySymbolStatus()) {
                lore.add(priceFormat + currencySymbol);
            } else {
                lore.add(priceFormat);
            }
            itemMeta.setLore(lore);
        } else {
            List<String> lore = new ArrayList<>();
            lore.add("");
            if (Config.getCurrencySymbolStatus()) {
                lore.add(priceFormat + currencySymbol);
            } else {
                lore.add(priceFormat);
            }
            itemMeta.setLore(lore);
        }
        item.setItemMeta(itemMeta);
        Main.getAllItems().add(item);
        Main.getCost().put(item, price);
        String message = plugin.getMessages().getString("item-added");
        ChatUtil.sendPrefixedMessage(sender, message);
    }

    @Subcommand("additem dummy")
    public void onAddItemDummy(final Player sender) {
        ItemStack dummy = new ItemStack(Extras.getColor(Config.getDummyColor()));
        Random dummyRan = plugin.getRandom();
        int dummyRanChoice = dummyRan.nextInt(1000000000);
        NBTItem nbtItem = new NBTItem(dummy);
        nbtItem.setInteger("RandomToPreventSimilarity", dummyRanChoice);
        dummy = nbtItem.getItem();
        Main.getAllItems().add(dummy);
        Main.getCost().put(dummy, 0);
        String message = plugin.getMessages().getString("item-added");
        ChatUtil.sendPrefixedMessage(sender, message);
    }


    @Subcommand("removeitem")
    public void onRemoveItem(final CommandSender sender, int id) {
        id = id - 1;
        if (!validId(id)) {
            String message = plugin.getMessages().getString("invalid-id");
            ChatUtil.sendPrefixedMessage(sender, message);
            return;
        }


        Main.getCost().remove(Main.getAllItems().get(id));
        Main.getAllItems().remove(id);
        String message = plugin.getMessages().getString("item-removed");
        ChatUtil.sendPrefixedMessage(sender, message);
    }

    private boolean validId(int id) {
        if (!(id + 1 > 0 && Main.getAllItems().size() >= id + 1)) {
            return false;
        }

        return Main.getCost().containsKey(Main.getAllItems().get(id));
    }


    @Subcommand("amount")
    @CommandPermission("mobcoinsplus.amount")
    public void onAmount(final CommandSender sender, final OnlinePlayer target) {
        int playerCoins = Extras.getCoins(target.getPlayer().getUniqueId());
        String message = plugin.getMessages().getString("player-balance")
                .replace("{total-coins}", Integer.toString(playerCoins))
                .replace("{currency}", Config.getCurrencyName())
                .replace("{player}", target.getPlayer().getName());

        ChatUtil.sendPrefixedMessage(sender,message);
    }

    @Subcommand("remove")
    @CommandPermission("mobcoinsplus.admin")
    public void onRemove(final CommandSender sender, final OnlinePlayer target, final int newAmount) {
        int playerCoins = Extras.getCoins(target.getPlayer().getUniqueId());
        if ((playerCoins - newAmount) < 0) {
            String message = plugin.getMessages().getString("target-negative-coins");
            ChatUtil.sendPrefixedMessage(sender, message);
            return;
        }

        Extras.removeCoins(target.getPlayer(), newAmount);
        String message = plugin.getMessages().getString("player-new-balance")
                .replace("{total-coins}", Integer.toString(Extras.getCoins(target.getPlayer().getUniqueId())))
                .replace("{currency}", Config.getCurrencyName())
                .replace("{player}", target.getPlayer().getName());

        ChatUtil.sendPrefixedMessage(sender,message);
    }

    @Subcommand("give")
    @CommandPermission("mobcoinsplus.admin")
    public void onGive(final CommandSender sender, final OnlinePlayer target, int newAmount) {
        Extras.giveCoins(target.getPlayer(), newAmount);
        int playerCoins = Extras.getCoins(target.getPlayer().getUniqueId());
        String message = plugin.getMessages().getString("player-new-balance")
                .replace("{total-coins}", Integer.toString(playerCoins))
                .replace("{currency}", Config.getCurrencyName())
                .replace("{player}", target.getPlayer().getName());

        ChatUtil.sendPrefixedMessage(sender,message);
    }

    @Default
    public void onBalance(final Player sender) {
        int playerCoins = Extras.getCoins(sender.getUniqueId());
        String message = plugin.getMessages().getString("self-check")
                .replace("{total-coins}", Integer.toString(playerCoins))
                .replace("{currency}", Config.getCurrencyName());
        ChatUtil.sendPrefixedMessage(sender, message);
    }

    @Subcommand("edit")
    public void onEdit(final Player sender, final int cost) {
        ItemStack item = Extras.duplicateItem(sender);

        boolean exists = false;
        for (ItemStack itemStack : Main.getCost().keySet()) {
            ItemStack copy = itemStack.clone();

            if (copy.isSimilar(item)) {
                Main.getCost().put(itemStack, cost);
                exists = true;
            }
        }
        if (!exists) {
            String message = plugin.getMessages().getString("new-item");
            ChatUtil.sendPrefixedMessage(sender, message);
        }
    }
}