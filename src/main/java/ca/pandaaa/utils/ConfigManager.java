package ca.pandaaa.utils;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.Set;

public class ConfigManager {
    // Configuration files //
    private final FileConfiguration configuration;
    private final FileConfiguration items;

    // Constructor //
    public ConfigManager(FileConfiguration configuration, FileConfiguration items) {
        this.configuration = configuration;
        this.items = items;
    }

    // Returns the unknown command message //
    public String getUnknownCommandMessage() {
        return Utils.applyFormat(configuration.getString("unknown-command"));
    }

    // Returns the no permission message //
    public String getNoPermissionMessage() {
        return Utils.applyFormat(configuration.getString("no-permission"));
    }

    // Returns the incorrect give argument(s) message //
    public String getIncorrectGiveArgumentsMessage() {
        return Utils.applyFormat(configuration.getString("incorrect-give-arguments"));
    }

    // Returns an array with the item names (identificators) //
    public String[] getItemNames() {
        Set<String> itemNamesSet = items.getConfigurationSection("items").getKeys(false);
        String[] itemNamesList = new String[itemNamesSet.size()];
        return itemNamesSet.toArray(itemNamesList);
    }

    // Returns the item name (ItemStack name) //
    public String getItemName(String itemName) {
        return items.getString("items." + itemName + ".name");
    }

    // Returns the description of the item //
    public List<String> getItemDescription(String itemName) {
        return items.getStringList("items." + itemName + ".description");
    }

    // Returns the list of lores applied on the ItemStack //
    public List<String> getItemLore(String itemName) {
        return items.getStringList("items." + itemName + ".lore");
    }

    // Returns the material of the item (if valid!) //
    public Material getItemMaterial(String itemName) {
        try {
            return Material.valueOf(items.getString("items." + itemName + ".item"));
        } catch(Exception exception) {
            return null;
        }
    }

    // Returns true if the item should be enchanted //
    public boolean getItemEnchanted(String itemName) {
        return items.getBoolean("items." + itemName + ".enchanted");
    }

    // Returns true if the item should be one-time-use (deleted after use) //
    public boolean getItemOneTimeUse(String itemName) {
        return items.getBoolean("items." + itemName + ".one-time-use");
    }

    // Returns the list of commands that will be executed by the console on use of the item //
    public List<String> getItemConsoleCommands(String itemName) {
        return items.getStringList("items." + itemName + ".console-commands");
    }

    // Returns the list of commands that will be executed by the player on use of the item //
    public List<String> getItemPlayerCommands(String itemName) {
        return items.getStringList("items." + itemName + ".player-commands");
    }

    // Returns the list of messages that will be sent to the player on use of the item //
    public List<String> getItemMessages(String itemName) {
        return items.getStringList("items." + itemName + ".messages");
    }

    // Returns the list of broadcasts that will be sent to the server on use of the item //
    public List<String> getItemBroadcasts(String itemName) {
        return items.getStringList("items." + itemName + ".broadcasts");
    }

    // Returns true if the player should be exempted from the broadcast //
    public boolean getItemBroadcastExempt(String itemName) {
        return items.getBoolean("items." + itemName + ".broadcast-exempt");
    }

    // Returns the item sound (if valid!) //
    public Sound getItemSound(String itemName) {
        try {
            return Sound.valueOf(items.getString("items." + itemName + ".sound"));
        } catch(Exception exception) {
            return null;
        }
    }

    // Returns the incorrect give player message //
    public String getIncorrectGivePlayerMessage(String player) {
        return Utils.applyFormat(configuration.getString("incorrect-give-player").replaceAll("%player%", player));
    }

    // Returns the incorrect give item message //
    public String getIncorrectGiveItemMessage(String item) {
        return Utils.applyFormat(configuration.getString("incorrect-give-item").replaceAll("%item%", item));
    }

    // Returns the incorrect give number message //
    public String getIncorrectGiveNumberMessage(String number) {
        return Utils.applyFormat(configuration.getString("incorrect-give-number").replaceAll("%number%", number));
    }

    // Returns the unallowed give number message //
    public String getUnallowedGiveNumberMessage(String number) {
        return Utils.applyFormat(configuration.getString("unallowed-give-number").replaceAll("%number%", number));
    }

    // Returns the plugin reload command message //
    public String getPluginReloadMessage() {
        return Utils.applyFormat(configuration.getString("plugin-reload"));
    }

    // Returns the format for the item listing (item "title") //
    public String getListNameMessage(String item, String itemName) {
        return Utils.applyFormat(configuration.getString("list-name").replaceAll("%item%", itemName).replaceAll("%item_id%", item));
    }

    // Returns the format for the item listing (item description) //
    public String getListDescriptionMessage(String description) {
        return Utils.applyFormat(configuration.getString("list-description").replaceAll("%description%", description));
    }

    // Returns the message sent when receiving an item //
    public String getItemReceivedMessage(String sender, String item, String itemName, String amount) {
        return Utils.applyFormat(configuration.getString("item-received-message").replaceAll("%amount%", amount).replaceAll("%sender%", sender).replaceAll("%item%", itemName).replaceAll("%item_id%", item));
    }

    // Returns the message sent when giving an item //
    public String getItemSentMessage(String receiver, String item, String itemName, String amount) {
        return Utils.applyFormat(configuration.getString("item-sent-message").replaceAll("%amount%", amount).replaceAll("%receiver%", receiver).replaceAll("%item%", itemName).replaceAll("%item_id%", item));
    }
}