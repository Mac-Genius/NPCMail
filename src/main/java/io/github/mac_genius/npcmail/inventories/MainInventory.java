package io.github.mac_genius.npcmail.inventories;

import io.github.mac_genius.npcmail.NPCMail;
import io.github.mac_genius.npcmail.database.models.Mail;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Mac on 3/28/2016.
 */
public class MainInventory implements Listener {
    private Player player;
    private int updateInvTask;

    public MainInventory(Player player) {
        this.player = player;
        openInventory();
        updateInvTask = NPCMail.getSingleton().getServer().getScheduler().runTaskTimerAsynchronously(NPCMail.getSingleton(), () -> {
            updateInventory();
        }, 0, 20).getTaskId();
    }


    @EventHandler
    public void clickItem(InventoryClickEvent event) {
        if (event.getWhoClicked().equals(player)) {
            if (event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta()) {
                Mail m = NPCMail.getSingleton().getMail().getMail(event.getCurrentItem().getItemMeta().getDisplayName(), true);
                if (m != null) {
                    if (m.getType().equalsIgnoreCase("simple")) {
                        if (event.getCurrentItem().getType() == Material.CHEST) {
                            NPCMail.getSingleton().getEconomy().depositMoney(player, m.getWorth());
                            NPCMail.getSingleton().getMail().updatePlayerMail(player, m.getName(), false, System.currentTimeMillis());
                            HandlerList.unregisterAll(this);
                            player.sendMessage(m.getDoneMessage());
                            event.setCancelled(true);
                            NPCMail.getSingleton().getServer().getScheduler().cancelTask(updateInvTask);
                            player.closeInventory();
                        } else {
                            player.sendMessage("You have already collected this item!");
                            event.setCancelled(true);
                            HandlerList.unregisterAll(this);
                            NPCMail.getSingleton().getServer().getScheduler().cancelTask(updateInvTask);
                            player.closeInventory();
                        }
                    }
                }
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void closeInventory(InventoryCloseEvent event) {
        if (event.getPlayer().equals(player)) {
            HandlerList.unregisterAll(this);
            NPCMail.getSingleton().getServer().getScheduler().cancelTask(updateInvTask);
        }
    }

    private void openInventory() {
        Inventory inventory = Bukkit.createInventory(player, 27, "Your Mail");
        ItemStack[] inv = NPCMail.getSingleton().getMail().getPlayerMail(player).getMainMenu();
        for (int i = 0; i < inv.length; i++) {
            inventory.setItem(i, inv[i]);
        }

        player.openInventory(inventory);
    }

    private void updateInventory() {
        Inventory inventory = player.getOpenInventory().getTopInventory();
        ItemStack[] inv = NPCMail.getSingleton().getMail().getPlayerMail(player).getMainMenu();
        for (int i = 0; i < inv.length; i++) {
            inventory.setItem(i, inv[i]);
        }
    }
}
