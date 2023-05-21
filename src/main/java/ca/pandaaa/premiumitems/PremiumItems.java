package ca.pandaaa.premiumitems;

import ca.pandaaa.commands.Commands;
import ca.pandaaa.commands.TabCompletion;
import ca.pandaaa.listeners.ClickListener;
import ca.pandaaa.utils.ConfigManager;
import ca.pandaaa.utils.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class PremiumItems extends JavaPlugin {
    // Generates the items.yml file //
    private File itemsFile;
    private FileConfiguration itemsConfig;

    // Attributes //
    private static PremiumItems plugin;
    private ConfigManager configManager;
    private ItemsManager itemsManager;

    // What should happen when the plugin enables //
    @Override
    public void onEnable() {
        // Plugin initialization //
        plugin = this;

        // BStats initialization //
        int pluginId = 18089;
        Metrics metrics = new Metrics(this, pluginId);

        // Configurations initialization //
        saveDefaultConfigurations();
        loadItemsConfig();
        configManager = new ConfigManager(getConfig(), itemsConfig);
        itemsManager = new ItemsManager(configManager);

        // Creates the command //
        getServer().getPluginManager().registerEvents(new ClickListener(), this);
        getCommands();

        // Console message when the plugin is fully enabled //
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "   &5_____  &d_____ "));
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &5|  _  |&d|     |     &5Prem&dium&8Ite&7ms"));
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &5|   __|&d|-   -|    &7Version " + getDescription().getVersion()));
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &5|__|   &d|_____|      &7by &8Pa&7nd&5aaa"));
        getServer().getConsoleSender().sendMessage("");
    }


    // Saves the default configuration files (creates them in the case that they don't already exist) //
    private void saveDefaultConfigurations() {
        this.saveDefaultConfig();
        itemsFile = new File(getDataFolder(), "items.yml");
        if (!itemsFile.exists())
            saveResource("items.yml", false);
        itemsConfig = new YamlConfiguration();
    }

    // Tries to load the items.yml file //
    private void loadItemsConfig() {
        try {
            itemsConfig.load(itemsFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    // Creates or changes the command //
    private void getCommands() {
        getCommand("PremiumItems").setExecutor(new Commands());
        getCommand("PremiumItems").setTabCompleter(new TabCompletion());
    }

    // Returns the plugin //
    public static PremiumItems getPlugin() {
        return plugin;
    }

    // Returns the configuration manager (the same one is use troughout the entire plugin) //
    public ConfigManager getConfigManager() {
        return configManager;
    }

    // Reloads the configurations //
    public void reloadConfig(CommandSender sender) {
        // Deletes the config data (not in the file but in the program) //
        plugin.reloadConfig();
        // Replaces the messages data by the ones from the file //
        itemsConfig = YamlConfiguration.loadConfiguration(itemsFile);

        // Replaces the commands and attributes //
        configManager = new ConfigManager(getConfig(), itemsConfig);
        itemsManager = new ItemsManager(configManager);
        HandlerList.unregisterAll();
        getServer().getPluginManager().registerEvents(new ClickListener(), this);
        getCommands();

        // Sends the confirmation message to the command executor //
        sender.sendMessage(configManager.getPluginReloadMessage());
    }

    // Returns the items manager (the same one is use troughout the entire plugin) //
    public ItemsManager getItemsManager() {
        return itemsManager;
    }

    // TODO Cooldowns on Item use?
    // TODO Number of Item use?
    // TODO Placeholder API?
    // TODO Item %item% and %item_id% in item description, item messages and broadcast?
}