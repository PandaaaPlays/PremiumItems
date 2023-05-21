package ca.pandaaa.premiumitems;

import ca.pandaaa.utils.ConfigManager;
import ca.pandaaa.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;
import java.util.stream.Collectors;

public class Items {
    // Class instances //
    private final ConfigManager config;

    // ItemStack attributes //
    private ItemStack item;
    private ItemMeta itemMeta;
    private Material material;
    private String itemStackName;
    private ArrayList<String> lore;
    private boolean isEnchanted;

    // Item attributes //
    private final String itemName;
    private Sound sound;
    private boolean oneTimeUse;
    private boolean broadcastExempt;
    private List<String> description;
    private List<String> consoleCommands;
    private List<String> playerCommands;
    private List<String> messages;
    private List<String> broadcasts;

    // Item instance creator //
    public Items(String itemIdentificator, ConfigManager configManager) {
        // Stores basic informations //
        itemName = itemIdentificator;
        config = configManager;

        // Creates & store the item ItemStack //
        createItemStackAttributes();
        createItemStack();

        // Stores configuration settings & messages //
        createConfigAttributes();
    }

    // Creates the attributes that are needed to create the item ItemStack //
    private void createItemStackAttributes() {
        material = config.getItemMaterial(itemName);
        itemStackName = Utils.applyFormat(config.getItemName(itemName));
        lore = getItemLore();
        isEnchanted = config.getItemEnchanted(itemName);
    }

    // Creates and returns the item lore as a List //
    private ArrayList<String> getItemLore() {
        ArrayList<String> loreList = new ArrayList<>();
        for(String loreMessage : config.getItemLore(itemName))
            loreList.add(Utils.applyFormat(loreMessage));
        return loreList;
    }

    // Creates and returns the ItemStack //
    private void createItemStack() {
        item = new ItemStack(material);

        itemMeta = item.getItemMeta();


        if (itemMeta == null)
            return;
        itemMeta.setDisplayName(itemStackName);
        itemMeta.setLore(lore);
        if (isEnchanted) {
            itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        if(material.equals(Material.PLAYER_HEAD)) {
            SkullMeta skullMeta = (SkullMeta) itemMeta;
            skullMeta.setOwningPlayer(Bukkit.getPlayer(config.getItemSkullOwner(itemName)));
            item.setItemMeta(skullMeta);
        } else {
            item.setItemMeta(itemMeta);
        }
    }

    // Creates the item settings and messages attributes //
    private void createConfigAttributes() {
        description = config.getItemDescription(itemName);
        oneTimeUse = config.getItemOneTimeUse(itemName);
        consoleCommands = config.getItemConsoleCommands(itemName);
        playerCommands = config.getItemPlayerCommands(itemName);
        messages = config.getItemMessages(itemName);
        broadcasts = config.getItemBroadcasts(itemName);
        broadcastExempt = config.getItemBroadcastExempt(itemName);
        sound = config.getItemSound(itemName);
    }

    // Returns the meta of the item //
    public ItemMeta getItemMeta() {
        return itemMeta;
    }

    // Returns the name of the item //
    public String getName() {
        return itemName;
    }

    // Returns the name of the itemStack //
    public String getItemStackName() { return itemStackName; }

    // Returns the description (list) of the item //
    public List<String> getDescription() {
        if(description.isEmpty())
            return null;
        return description;
    }

    // Gives the item to a player (with a specified amount) //
    public void giveItem(Player player, Integer amount) {
        item.setAmount(amount);
        player.getInventory().addItem(item);
    }

    // Uses the item (can delete the item if it is one-time-use) //
    public void useItem(Player player) {
        // Sends the console commands (commands executed by the console) //
        if(!consoleCommands.isEmpty()) {
            for (String command : consoleCommands)
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
        }
        // Sends the player commands (commands executed by the player) //
        if(!playerCommands.isEmpty()) {
            for (String command : playerCommands)
                Bukkit.getServer().dispatchCommand(player, command);
        }
        // Sends the messages to the player //
        if(!messages.isEmpty()) {
            for (String message : messages)
                player.sendMessage(Utils.applyFormat(message));
        }
        // Sends the broadcasts to all the receivers //
        if(!broadcasts.isEmpty()) {
            for (Player players : getBroadcastReceivers(player)) {
                for (String broadcast : broadcasts)
                    players.sendMessage(Utils.applyFormat(broadcast.replace("%player%", player.getName())));
            }
        }

        // Removes the item if it is one-time-use //
        if(oneTimeUse)
            removeItem(player);

        // Sends the sound to the player if it is not null //
        if (sound != null)
            player.playSound(player.getLocation(), sound, 1, 1);
    }

    // Removes the item (once) from the player's inventory //
    private void removeItem(Player player) {
        item.setAmount(1);
        player.getInventory().removeItem(item);
    }

    // Returns a Collection of who should receive the broadcast //
    private Collection<? extends Player> getBroadcastReceivers(Player exempted) {
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        if (broadcastExempt)
            players = players.stream().filter(player -> !(player == exempted)).collect(Collectors.toList());
        return players;
    }

}
