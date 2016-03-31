package io.github.mac_genius.npcmail.utils;

import io.github.mac_genius.npcmail.NPCMail;
import io.github.mac_genius.npcmail.database.models.Mail;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

/**
 * Created by Mac on 3/28/2016.
 */
public class MailTools {
    public static ItemStack createItem(Mail mail) {
        ItemStack item = new ItemStack(Material.CHEST, 1, (short)0);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(mail.getDisplayName());
        meta.setLore(mail.getLore());
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItem(Mail mail, long lastCollected) {
        ItemStack item;
        long current = System.currentTimeMillis();
        long wait = mail.getRefresh() + lastCollected;
        if (mail.getRefresh() != 0) {
            if (wait <= current) {
                item = new ItemStack(Material.CHEST, 1, (short) 0);
            } else {
                item = new ItemStack(Material.ENDER_CHEST, 1, (short) 0);
            }
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(mail.getDisplayName());
            ArrayList<String> lore = new ArrayList<>(mail.getLore());
            lore.add("");
            lore.add(getTimeLeft(wait - current));
            meta.setLore(lore);
            item.setItemMeta(meta);
            return item;
        } else {
            return null;
        }
    }

    public static boolean canCollect(Mail mail, long lastCollected) {
        return (mail.getRefresh() != 0 && mail.getRefresh() + lastCollected <= System.currentTimeMillis());
    }

    public static String getColorPalette() {
        return ChatColor.BLACK + "&0 " + ChatColor.DARK_BLUE + "&1 " + ChatColor.DARK_GREEN + "&2 " + ChatColor.DARK_AQUA + "&3 "
                + ChatColor.DARK_RED + "&4 " + ChatColor.DARK_PURPLE + "&5 " + ChatColor.GOLD + "&6 " + ChatColor.GRAY + "&7 "
                + ChatColor.DARK_GRAY + "&8 " + ChatColor.BLUE + "&9 " + ChatColor.GREEN + "&a " + ChatColor.AQUA + "&b "
                + ChatColor.RED + "&c " + ChatColor.LIGHT_PURPLE + "&d " + ChatColor.YELLOW + "&e " + ChatColor.WHITE + "&f";
    }

    public static String getTimeLeft(long time) {
        if (time > 0) {
            long days = time / 86400000;
            if (days > 0) {
                return ChatColor.RED + "" + days + "d";
            }
            long hours = time / 3600000;
            if (hours > 0) {
                return ChatColor.RED + "" + hours + "h";
            }
            long minutes = time / 60000;
            if (minutes > 0) {
                return ChatColor.RED + "" + minutes + "m";
            }
            long seconds = time / 1000;
            if (seconds > 0) {
                return ChatColor.RED + "" + seconds + "s";
            }
        }
        return ChatColor.GREEN + "Collect now!";
    }
}
