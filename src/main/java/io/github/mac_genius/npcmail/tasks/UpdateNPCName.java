package io.github.mac_genius.npcmail.tasks;

import io.github.mac_genius.npcmail.NPCMail;
import io.github.mac_genius.npcmail.database.cache.PlayerMail;
import io.github.mac_genius.npcmail.npc.CustomPacket;
import io.github.mac_genius.npcmail.npc.MailMan;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

/**
 * Created by Mac on 3/27/2016.
 */
public class UpdateNPCName implements Runnable {
    private int num;
    private String current;

    public UpdateNPCName() {
        num = 0;
        current = "";
    }

    @Override
    public void run() {
        switch (num) {
            case 0:
                current = ChatColor.RED + "{player}, you have {amount} new item{plural} to collect!";
                num++;
                break;
            case 1:
                current = ChatColor.BLUE + "{player}, you have {amount} new item{plural} to collect!";
                num++;
                break;
            case 2:
                current = ChatColor.GOLD + "{player}, you have {amount} new item{plural} to collect!";
                num++;
                break;
            case 3:
                current = ChatColor.GREEN + "{player}, you have {amount} new item{plural} to collect!";
                num++;
                break;
            case 4 :
                current = ChatColor.LIGHT_PURPLE + "{player}, you have {amount} new item{plural} to collect!";
                num = 0;
                break;
        }
        ArmorStand stand = null;
        for (ArmorStand stand1 : NPCMail.getSingleton().getArmorStands()) {
            if (stand1.getCustomName().equals("{mail}")) {
                stand = stand1;
                break;
            }
        }
        if (stand != null) {
            updatePlayers(stand);
        }
    }

    private void updatePlayers(ArmorStand stand) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            DataWatcher watcher = new DataWatcher(((CraftArmorStand)stand).getHandle());
            PlayerMail mail = NPCMail.getSingleton().getMail().getPlayerMail(p);
            int amount = 0;
            if (mail != null) {
                amount = mail.getAvailableMail();
            }
            if (amount == 0) {
                watcher.a(2, "No new mail, " + p.getName());
            } else if (amount == 1) {
                watcher.a(2, current.replace("{player}", p.getName()).replace("{amount}", 1 + "").replace("{plural}", ""));
            } else {
                watcher.a(2, current.replace("{player}", p.getName()).replace("{amount}", amount + "").replace("{plural}", "s"));
            }
            ((CraftPlayer)p).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityMetadata(((CraftArmorStand)stand).getHandle().getId(), watcher, true));
        }
    }
}
