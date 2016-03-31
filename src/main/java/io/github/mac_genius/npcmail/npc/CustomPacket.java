package io.github.mac_genius.npcmail.npc;

import io.github.mac_genius.npcmail.utils.ReflectTools;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInSettings;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;

/**
 * Created by Mac on 3/27/2016.
 */
public class CustomPacket {
    public static PacketPlayInFlying getPositionPacket(double x, double y, double z, float yaw, float pitch, boolean onGround) {
        PacketPlayInFlying packet = new PacketPlayInFlying();
        ReflectTools.setObject(packet, "x", x);
        ReflectTools.setObject(packet, "y", y);
        ReflectTools.setObject(packet, "z", z);
        ReflectTools.setObject(packet, "yaw", yaw);
        ReflectTools.setObject(packet, "pitch", pitch);
        return packet;
    }
    public static PacketPlayInSettings getSettingsPacket(String field, Object value) {
        PacketPlayInSettings settings = new PacketPlayInSettings();
        ReflectTools.setObject(settings, field, value);
        return settings;
    }

    public static PacketPlayInSettings getSettingsPacket(PacketPlayInSettings packet, String field, Object value) {
        ReflectTools.setObject(packet, field, value);
        return packet;
    }
}
