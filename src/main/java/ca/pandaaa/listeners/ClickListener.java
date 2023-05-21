package ca.pandaaa.listeners;

import ca.pandaaa.premiumitems.ItemsManager;
import ca.pandaaa.premiumitems.PremiumItems;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.ItemMeta;

public class ClickListener implements Listener {
    // Class instances //
    ItemsManager itemsManager = PremiumItems.getPlugin().getItemsManager();

    // What should happen when a player right clicks (air or block!) //
    @EventHandler
    public void onClickEvent(PlayerInteractEvent event) {
        Action action = event.getAction();
        if(!action.equals(Action.RIGHT_CLICK_AIR) && !action.equals(Action.RIGHT_CLICK_BLOCK))
            return;

        if(!event.getHand().equals(EquipmentSlot.HAND))
            return;

        Player player = event.getPlayer();
        if(player.getInventory().getItemInMainHand().getType().equals(Material.AIR))
            return;

        // If the player is holding an item //
        playerItemInHand(player);
    }

    // Uses the item if it is PremiumItem //
    private void playerItemInHand(Player player) {
        ItemMeta itemMeta = player.getInventory().getItemInMainHand().getItemMeta();
        if(itemsManager.containsItemMeta(itemMeta)) {
            itemsManager.getItem(itemMeta).useItem(player);
        }
    }
}
