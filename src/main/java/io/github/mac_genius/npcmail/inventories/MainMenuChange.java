package io.github.mac_genius.npcmail.inventories;

import io.github.mac_genius.npcmail.NPCMail;
import io.github.mac_genius.npcmail.database.models.Mail;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Mac on 3/28/2016.
 */
public class MainMenuChange implements Listener {
    private Player player;

    public MainMenuChange(Player player) {
        this.player = player;
        openInventory();
    }

    @EventHandler
    public void closeInventory(InventoryCloseEvent event) {
        if (event.getPlayer().equals(player)) {
            HandlerList.unregisterAll(this);
            for (int i = 0; i < event.getInventory().getSize(); i++) {
                if (event.getInventory().getItem(i) != null) {
                    if (event.getInventory().getItem(i).hasItemMeta()) {
                        Mail m = NPCMail.getSingleton().getMail().getMail(event.getInventory().getItem(i).getItemMeta().getDisplayName(), true);
                        if (m != null) {
                            m.setSlot(i);
                            NPCMail.getSingleton().getMail().updateMail(m);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void playerDropItem(PlayerDropItemEvent event) {
        if (event.getPlayer().equals(player)) {
            event.setCancelled(true);
            player.sendMessage("You cannot do that while editing the menu.");
        }
    }

    private void openInventory() {
        Inventory inventory = Bukkit.createInventory(player, 27, "Edit the Menu:");
        ItemStack[] inv = NPCMail.getSingleton().getMail().getPlayerMail(player).getMainMenu();
        for (int i = 0; i < inv.length; i++) {
            inventory.setItem(i, inv[i]);
        }

        player.openInventory(inventory);
    }
}
