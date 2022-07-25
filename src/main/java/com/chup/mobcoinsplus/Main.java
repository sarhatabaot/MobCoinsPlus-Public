package com.chup.mobcoinsplus;

import co.aikar.commands.PaperCommandManager;
import com.chup.mobcoinsplus.configuration.ConfigManager;
import com.chup.mobcoinsplus.extras.SLAPI;
import com.chup.mobcoinsplus.extras.SpigotExpansion;
import com.chup.mobcoinsplus.guis.ClickListener;
import com.chup.mobcoinsplus.listeners.*;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class Main extends JavaPlugin {
    private static Map<UUID, Integer> points = new HashMap<>();
    private static List<ItemStack> allItems;
    private static Map<ItemStack, Integer> cost;
    private ConfigManager configManager;

    private Random random;

    public static Map<ItemStack, Integer> getCost() {
        return cost;
    }

    public static void setCost(Map<ItemStack, Integer> cost) {
        Main.cost = cost;
    }

    public static List<ItemStack> getAllItems() {
        return allItems;
    }

    public static void setAllItems(List<ItemStack> allItems) {
        Main.allItems = allItems;
    }

    public static Map<UUID, Integer> getPoints() {
        return points;
    }

    public static void setPoints(Map<UUID, Integer> points) {
        Main.points = points;
    }

    @Override
    public void onEnable() {
        this.random = new Random();
        setAllItems(new ArrayList<>());
        setCost(new HashMap<>());

        File file = new File(this.getDataFolder() + "/data");
        if(!file.exists()) {
            file.mkdir();
        }

        try {
            setPoints((HashMap<UUID, Integer>) SLAPI.load("./plugins/MobCoinsPlus/data/coins.bin"));
        } catch (FileNotFoundException e) {
            getLogger().info("Coins file generating!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            setAllItems((ArrayList<ItemStack>) SLAPI.bukkitLoad("./plugins/MobCoinsPlus/data/items.bin"));
        } catch (FileNotFoundException e) {
            getLogger().info("Items file generating!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            setCost((HashMap<ItemStack, Integer>) SLAPI.bukkitLoad("./plugins/MobCoinsPlus/data/cost.bin"));
        } catch (FileNotFoundException e) {
            getLogger().info("Cost file generating!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        getLogger().info("Enabled Successfully!");
        new Config(this);

        new Metrics(this, 9663);

        this.setConfigManager(new ConfigManager(this));
        this.getConfigManager().load("messages.yml");
        this.getConfigManager().save("messages.yml");

        PaperCommandManager paperCommandManager = new PaperCommandManager(this);
        paperCommandManager.registerCommand(new MobCoinsCommand(this));

        Bukkit.getPluginManager().registerEvents(new CoinListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ClickListener(this), this);
        Bukkit.getPluginManager().registerEvents(new CommandListener(), this);
        Bukkit.getPluginManager().registerEvents(new JoinListener(this), this);
        Bukkit.getPluginManager().registerEvents(new DeathListener(this), this);

        try {
            new SpigotExpansion().register();
        } catch (NoClassDefFoundError e) {
            getLogger().severe("Error: No placeholder plugin detected. Please install PAPI to use placeholders.");
        }
    }

    @Override
    public void onDisable() {
        this.random = null;
        try {
            SLAPI.save(getPoints(), "./plugins/MobCoinsPlus/data/coins.bin");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            SLAPI.bukkitSave(getAllItems(), "./plugins/MobCoinsPlus/data/items.bin");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            SLAPI.bukkitSave(getCost(), "./plugins/MobCoinsPlus/data/cost.bin");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String format(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public FileConfiguration getMessages() {
        return ConfigManager.get("messages.yml");
    }

    public Random getRandom() {
        return random;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public void setConfigManager(ConfigManager configManager) {
        this.configManager = configManager;
    }
}
