package com.chup.mobcoinsplus;

import com.chup.mobcoinsplus.configuration.ConfigManager;
import com.chup.mobcoinsplus.extras.SLAPI;
import com.chup.mobcoinsplus.extras.SpigotExpansion;
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

    public static Map<UUID, Integer> points = new HashMap<>();
    public static List<ItemStack> allItems;
    public static Map<ItemStack, Integer> cost;
    public ConfigManager configManager;

    private Random random;

    @Override
    public void onEnable() {
        this.random = new Random();
        allItems = new ArrayList<>();
        cost = new HashMap<>();

        File file = new File(this.getDataFolder() + "/data");
        if(!file.exists()) {
            file.mkdir();
        }

        try {
            points = (HashMap<UUID, Integer>) SLAPI.load("./plugins/MobCoinsPlus/data/coins.bin");
        } catch (FileNotFoundException e) {
            getLogger().info("Coins file generating!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            allItems = (ArrayList<ItemStack>) SLAPI.bukkitLoad("./plugins/MobCoinsPlus/data/items.bin");
        } catch (FileNotFoundException e) {
            getLogger().info("Items file generating!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            cost = (HashMap<ItemStack, Integer>) SLAPI.bukkitLoad("./plugins/MobCoinsPlus/data/cost.bin");
        } catch (FileNotFoundException e) {
            getLogger().info("Cost file generating!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        getLogger().info("Enabled Successfully!");
        new Config(this);

        new Metrics(this, 9663);

        this.configManager = new ConfigManager(this);
        this.configManager.load("messages.yml");
        this.configManager.save("messages.yml");

        getCommand("mobcoins").setExecutor(new MobCoinsExecutor(this));
        getCommand("mobshop").setExecutor(new MobShopExecutor(this));
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
            SLAPI.save(points, "./plugins/MobCoinsPlus/data/coins.bin");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            SLAPI.bukkitSave(allItems, "./plugins/MobCoinsPlus/data/items.bin");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            SLAPI.bukkitSave(cost, "./plugins/MobCoinsPlus/data/cost.bin");
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

}
