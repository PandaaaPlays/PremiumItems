package ca.pandaaa.commands;

import ca.pandaaa.premiumitems.Items;
import ca.pandaaa.premiumitems.ItemsManager;
import ca.pandaaa.premiumitems.PremiumItems;
import ca.pandaaa.utils.ConfigManager;
import ca.pandaaa.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
    // Class instances //
    PremiumItems plugin = PremiumItems.getPlugin();
    ConfigManager configManager = plugin.getConfigManager();
    ItemsManager itemsManager = plugin.getItemsManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String message, String[] args) {
        // If the sender is neither a player nor the console, cancel! //
        if (!(sender instanceof Player) && !(sender instanceof ConsoleCommandSender)) return false;

        if (command.getName().equalsIgnoreCase("premiumitems")) {
            // If there are no arguments, sends the error and return false //
            if (args.length == 0) {
                sendUnknownCommandMessage(sender);
                return false;
            }

            // Checks the first argument //
            switch (args[0].toLowerCase()) {
                // "reload" will reload the configurations //
                case "reload":
                    reloadPlugin(sender);
                    break;
                // "list" will print all the items in the sender's chat //
                case "list":
                    itemList(sender);
                    break;
                // "give" will give the item to the specified player //
                case "give":
                    giveItem(sender, args);
                    break;
                // Anything else will send the error //
                default:
                    sendUnknownCommandMessage(sender);
                    break;
            }
        }
        return false;
    }

    // Reloads the plugin //
    public void reloadPlugin(CommandSender sender) {
        if (!sender.hasPermission("premiumitems.config")) {
            sendNoPermissionMessage(sender);
            return;
        }

        // Reloads the configurations and sends the confirmation message //
        plugin.reloadConfig(sender);
    }

    // Items listing : Sends a list of all the available items to the sender //
    private void itemList(CommandSender sender) {
        if (!sender.hasPermission("premiumitems.config")) {
            sendNoPermissionMessage(sender);
            return;
        }
        // If no item is specified in the configuration //
        if(itemsManager.isEmpty())
            return;

        // Sends the listing messages to the sender //
        itemsManager.sendListMessages(sender);
    }

    // Give item : Gives the specified player the item //
    private void giveItem(CommandSender sender, String[] args) {
        if (!sender.hasPermission("premiumitems.config")) {
            sendNoPermissionMessage(sender);
            return;
        }

        // Command : /premiumitems give //
        if(args.length != 4) {
            sendIncorrectGiveArgumentsMessage(sender);
            return;
        }

        String playerString = args[1];
        String itemID = args[2];
        String amount = args[3];

        // Command : /premiumitems give %incorrect_player% //
        if(Bukkit.getPlayer(playerString) == null) {
            sendIncorrectGivePlayerMessage(sender, playerString);
            return;
        }
        Player player = Bukkit.getPlayer(playerString);

        // Command: /premiumitems give %player% %incorrect_item% //
        if(!itemsManager.contains(itemID)) {
            sendIncorrectGiveItemMessage(sender, itemID);
            return;
        }

        // Command: /premiumitems give %player% %item% %incorrect_number% //
        if(!Utils.isInteger(amount)) {
            sendIncorrectGiveNumberMessage(sender, amount);
            return;
        }

        // Command: /premiumitems give %player% %item% %unallowed_number% //
        if(Integer.parseInt(amount) < 1 || Integer.parseInt(amount) > 64) {
            sendUnallowedGiveNumberMessage(sender, amount);
            return;
        }

        // Gets the Items instance for the item name (ID) specified //
        Items item = itemsManager.getItem(itemID);

        // Gives the items to the player //
        item.giveItem(player, Integer.parseInt(amount));

        // Sends the message to the sender & the receiver! //
        sender.sendMessage(configManager.getItemSentMessage(player.getName(), itemID, item.getName(), amount));
        String senderName = "Console";
        if(sender instanceof Player)
            senderName = sender.getName();
        player.sendMessage(configManager.getItemReceivedMessage(senderName, itemID, item.getName(), amount));
    }

    // Sends the unknownCommand error message //
    private void sendUnknownCommandMessage(CommandSender sender) {
        sender.sendMessage(configManager.getUnknownCommandMessage());
    }

    // Sends the noPermission error message //
    private void sendNoPermissionMessage(CommandSender sender) {
        sender.sendMessage(configManager.getNoPermissionMessage());
    }

    // Sends the incorrect give arguments message //
    private void sendIncorrectGiveArgumentsMessage(CommandSender sender) {
        sender.sendMessage(configManager.getIncorrectGiveArgumentsMessage());
    }

    // Sends the incorrect give player message //
    private void sendIncorrectGivePlayerMessage(CommandSender sender, String player) {
        sender.sendMessage(configManager.getIncorrectGivePlayerMessage(player));
    }

    // Sends the incorrect give item message //
    private void sendIncorrectGiveItemMessage(CommandSender sender, String item) {
        sender.sendMessage(configManager.getIncorrectGiveItemMessage(item));
    }

    // Sends the incorrect give number message //
    private void sendIncorrectGiveNumberMessage(CommandSender sender, String number) {
        sender.sendMessage(configManager.getIncorrectGiveNumberMessage(number));
    }

    // Sends the unallowed give number message //
    private void sendUnallowedGiveNumberMessage(CommandSender sender, String number) {
        sender.sendMessage(configManager.getUnallowedGiveNumberMessage(number));
    }
}