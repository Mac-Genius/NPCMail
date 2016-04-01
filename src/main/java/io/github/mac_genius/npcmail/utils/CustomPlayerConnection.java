package io.github.mac_genius.npcmail.utils;

import io.github.mac_genius.npcmail.NPCMail;
import io.github.mac_genius.npcmail.npc.CustomPacket;
import net.minecraft.server.v1_9_R1.*;

/**
 * Created by Mac on 3/27/2016.
 */
public class CustomPlayerConnection extends PlayerConnection {
    public CustomPlayerConnection(MinecraftServer minecraftserver, NetworkManager networkmanager, EntityPlayer entityplayer) {
        super(minecraftserver, networkmanager, entityplayer);
        NPCMail.getSingleton().getServer().getScheduler().runTaskTimerAsynchronously(NPCMail.getSingleton(), () -> {
            a(new PacketPlayInKeepAlive());
        }, 0, 20);
        NPCMail.getSingleton().getServer().getScheduler().runTaskLater(NPCMail.getSingleton(), () -> {
            a(CustomPacket.getPositionPacket(getPlayer().getLocation().getX(), getPlayer().getLocation().getY(), getPlayer().getLocation().getZ(), getPlayer().getLocation().getYaw(), getPlayer().getLocation().getPitch(), getPlayer().isOnGround()));
            a(CustomPacket.getSettingsPacket("e", 127));
        }, 10);
    }

    @Override
    public void sendPacket(Packet packet) {
        // Don't do anything because it's a NPC.
    }

    public static void swapConnection(EntityPlayer player) {
        player.playerConnection = new CustomPlayerConnection(player.server, new NetworkManager(EnumProtocolDirection.SERVERBOUND), player);
    }
}
