package ca.pandaaa.commands;

import ca.pandaaa.premiumitems.Items;
import ca.pandaaa.premiumitems.ItemsManager;
import ca.pandaaa.premiumitems.PremiumItems;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TabCompletion implements TabCompleter {
    // Class instances //
    ItemsManager itemsManager = PremiumItems.getPlugin().getItemsManager();

    // Proposes completion for the PremiumItems command //
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String arg, String[] args) {
        List<String> completionList = new ArrayList<>();
        if (sender.hasPermission("premiumitems.config")) {
            // Completion : /premiumitems < >
            if (args.length == 1) {
                completionList.add("give");
                completionList.add("list");
                completionList.add("reload");
            } else if (args.length == 2) {
                // Completion : /premiumitems give < >
                if (args[0].equalsIgnoreCase("give")) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        completionList.add(player.getName());
                    }
                    return completionList;
                }
            } else if (args.length == 3) {
                // Completion : /premiumitems give ... < >
                if (args[0].equalsIgnoreCase("give")) {
                    completionList.addAll(itemsManager.getItemNameList());
                    return completionList;
                }
            }
        }
        return completionList;
    }
}