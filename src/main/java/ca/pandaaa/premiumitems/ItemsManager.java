package ca.pandaaa.premiumitems;

import ca.pandaaa.utils.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.HashMap;
import java.util.Set;

public class ItemsManager {
    // Class instances //
    private ConfigManager config;

    // Item lists (same lists for all items) //
    private HashMap<String, Items>  itemNames = new HashMap<>();
    private HashMap<ItemMeta, Items> itemMetas = new HashMap<>();

    public ItemsManager(ConfigManager configManager) {
        // Stores basic informations //
        config = configManager;

        for (String itemName : configManager.getItemNames()) {
            // Stores the item to be used later //
            Items item = new Items(itemName, configManager);
            itemNames.put(itemName, item);
            itemMetas.put(item.getItemMeta(), item);
        }
    }

    // Returns true if the item exists //
    public boolean contains(String name) {
        return itemNames.containsKey(name);
    }

    // Returns true if the itemStack exists //
    public boolean containsItemMeta(ItemMeta itemMeta) {
        if(itemMeta instanceof SkullMeta) {
            // The itemMeta stores the texture but the list does not... (this removes the texture) //
            ((SkullMeta) itemMeta).setOwningPlayer(((SkullMeta) itemMeta).getOwningPlayer());
        }
        return itemMetas.containsKey(itemMeta);
    }

    // Is the item list empty (configuration contains no items) //
    public boolean isEmpty() {
        return itemNames.isEmpty();
    }

    // Returns the item associated with the name //
    public Items getItem(String name) {
        if(!contains(name))
            return null;
        return itemNames.get(name);
    }

    // Returns the item associated with the itemMeta //
    public Items getItem(ItemMeta itemMeta) {
        if(!containsItemMeta(itemMeta))
            return null;
        return itemMetas.get(itemMeta);
    }

    // Returns a Set of all the item names //
    public Set<String> getItemNameList() {
        if(isEmpty())
            return null;
        return itemNames.keySet();
    }

    // Sends the item listing messages to the sender //
    public void sendListMessages(CommandSender sender) {
        for(Items item : itemNames.values()) {
            sender.sendMessage(config.getListNameMessage(item.getName(), item.getItemStackName()));
            for(String descritpionLine : item.getDescription())
                sender.sendMessage(config.getListDescriptionMessage(descritpionLine));
        }
    }

}
