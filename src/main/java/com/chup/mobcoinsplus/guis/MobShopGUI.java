package com.chup.mobcoinsplus.guis;

import com.chup.mobcoinsplus.Config;
import com.chup.mobcoinsplus.Main;
import com.chup.mobcoinsplus.extras.ChatUtil;
import com.chup.mobcoinsplus.extras.Extras;
import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MobShopGUI {
    private final PaginatedGui gui;
    private final Main plugin;
    private final String prefix = ChatUtil.color(Config.getPluginPrefix());
    final DecimalFormat formatter = new DecimalFormat("###,###,###,###,###,###,###,###");

    public MobShopGUI(final Main plugin) {
        this.plugin = plugin;
        final String name = Config.getMobShopGUIName();
        this.gui = Gui.paginated()
                .title(Component.text(name))
                .rows(6)
                .create();

        gui.setDefaultClickAction(event -> event.setCancelled(true));

        gui.addItem(getAsGuiItems().toArray(new GuiItem[]{}));

        GuiItem previous = ItemBuilder.from(XMaterial.ARROW.parseMaterial())
                .name(Component.text("Previous"))
                .lore(Component.text("Page - " + gui.getPrevPageNum()))
                .asGuiItem(event -> gui.previous());

        GuiItem next = ItemBuilder.from(XMaterial.ARROW.parseMaterial())
                .name(Component.text("Next"))
                .lore(Component.text("Page - " + gui.getNextPageNum()))
                .asGuiItem(event -> gui.next());

        gui.setItem(48, previous);
        gui.setItem(50, next);

        GuiItem fillerItem = ItemBuilder.from(Extras.getColor(Config.getBorderColor())).asGuiItem();
        this.gui.getFiller().fill(fillerItem);
    }

    private @NotNull List<GuiItem> getAsGuiItems() {
        List<GuiItem> guiItems = new ArrayList<>();

        GuiAction<InventoryClickEvent> buyAction = event -> {
            final Player player = (Player) event.getWhoClicked();
            if (!Main.getPoints().containsKey(player.getUniqueId())) {
                String message = plugin.getMessages().getString("insufficient-coins");
                player.sendMessage(prefix + ChatUtil.color(message));
                player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                return;
            }

            if (!Main.getCost().containsKey(event.getCurrentItem())) {
                String message = plugin.getMessages().getString("item-unknown");
                player.sendMessage(prefix + ChatUtil.color(message));
                player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                player.closeInventory();
                return;
            }
            if (Main.getPoints().get(player.getUniqueId()) < Main.getCost().get(event.getCurrentItem())) {
                String message = plugin.getMessages().getString("insufficient-coins");
                player.sendMessage(prefix + ChatUtil.color(message));
                player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                return;
            }

            int price = Main.getCost().get(event.getCurrentItem());
            int balance = Main.getPoints().get(player.getUniqueId());

            if (player.getInventory().firstEmpty() == -1) {
                String message = plugin.getMessages().getString("inventory-full");
                player.sendMessage(prefix + ChatUtil.color(message));
                player.playSound(player.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 1.0F, 1.0F);
                return;
            }

            Main.getPoints().put(player.getUniqueId(), balance - price);
            player.getInventory().setItem(player.getInventory().firstEmpty(), event.getCurrentItem().clone());
            player.closeInventory();
            String message = plugin.getMessages().getString("item-purchased");
            player.sendMessage(prefix + ChatUtil.color(message));
            player.playSound(player.getLocation(), XSound.ENTITY_PLAYER_LEVELUP.parseSound(), 1.0F, 1.0F);
        };

        for (ItemStack itemStack : Main.getAllItems()) {
            guiItems.add(ItemBuilder.from(itemStack)
                    .asGuiItem(buyAction));
        }
        return guiItems;
    }

    public void openGui(final Player player, final int page) {
        GuiItem coinsDisplay = ItemBuilder.from(XMaterial.PRISMARINE_CRYSTALS.parseMaterial())
                .name(Component.text(getCoinsDisplayName(player))).asGuiItem();

        this.gui.setItem(49, coinsDisplay);

        this.gui.open(player, page);
    }

    private @NotNull String getCoinsDisplayName(final @NotNull Player player) {
        final String currency = Config.getCurrencyName();
        final String currencySymbol = Config.getCurrencySymbol();
        final int amount = getPointAmount(player.getUniqueId());
        if (Main.getPoints().containsKey(player.getUniqueId())) {
            if (Config.getCurrencySymbolStatus()) {
                return ChatColor.AQUA + currency + ": " + ChatColor.GRAY + formatter.format(amount) + currencySymbol;
            }
            return ChatColor.AQUA + currency + ": " + ChatColor.GRAY + formatter.format(amount);
        }

        if (Config.getCurrencySymbolStatus()) {
            return ChatColor.AQUA + currency + ": " + ChatColor.GRAY + "0" + currencySymbol;
        }

        return ChatColor.AQUA + currency + ": " + ChatColor.GRAY + "0";
    }

    private int getPointAmount(final UUID uuid) {
        if (Main.getPoints().containsKey(uuid)) {
            return Main.getPoints().get(uuid);
        }
        return 0;
    }
}