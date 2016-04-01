package io.github.mac_genius.npcmail.tasks;

import io.github.mac_genius.npcmail.NPCMail;
import net.minecraft.server.v1_9_R1.EntityPlayer;
import net.minecraft.server.v1_9_R1.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_9_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_9_R1.PacketPlayOutPlayerInfo;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Created by Mac on 3/27/2016.
 */
public class ShowPlayer implements Runnable {
    private ArrayList<Player> seePlayer;

    public ShowPlayer() {
        seePlayer = new ArrayList<>();
    }

    @Override
    public void run() {
        for (Player p : new ArrayList<>(seePlayer)) {
            if (!p.getNearbyEntities(40, 40, 40).contains(NPCMail.getSingleton().getMailMan())) {
                seePlayer.remove(p);
            }
        }
        for (Entity e : NPCMail.getSingleton().getMailMan().getNearbyEntities(40, 40, 40)) {
            if (e instanceof Player) {
                if (!seePlayer.contains(e)) {
                    EntityPlayer npc = NPCMail.getSingleton().getMailMan().getHandle();
                    seePlayer.add((Player) e);
                    ((CraftPlayer)e).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
                    ((CraftPlayer)e).getHandle().playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
                    ((CraftPlayer)e).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte)160));
                    NPCMail.getSingleton().getServer().getScheduler().runTaskLater(NPCMail.getSingleton(), () -> {
                        ((CraftPlayer) e).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc));
                    }, 10);
                }
            }
        }
    }
}
