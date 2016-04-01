package io.github.mac_genius.npcmail.listeners;

import io.github.mac_genius.npcmail.NPCMail;
import io.github.mac_genius.npcmail.inventories.MainInventory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Mac on 3/27/2016.
 */
public class PlayerListeners implements Listener {
    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        NPCMail.getSingleton().getServer().getScheduler().runTaskAsynchronously(NPCMail.getSingleton(), () -> {
            NPCMail.getSingleton().getMail().loadPlayer(event.getPlayer());
        });
    }

    @EventHandler
    public void playerClickNPC(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked().equals(NPCMail.getSingleton().getMailMan())) {
            NPCMail.getSingleton().getServer().getPluginManager().registerEvents(new MainInventory(event.getPlayer()), NPCMail.getSingleton());
        }
    }

    @EventHandler
    public void playerLeave(PlayerQuitEvent event) {
        NPCMail.getSingleton().getServer().getScheduler().runTaskAsynchronously(NPCMail.getSingleton(), () -> {
            NPCMail.getSingleton().getMail().unloadPlayer(event.getPlayer());
        });
    }

    @EventHandler
    public void playerKick(PlayerKickEvent event) {
        NPCMail.getSingleton().getServer().getScheduler().runTaskAsynchronously(NPCMail.getSingleton(), () -> {
            NPCMail.getSingleton().getMail().unloadPlayer(event.getPlayer());
        });
    }
}
