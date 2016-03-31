package io.github.mac_genius.npcmail.npc;

import com.google.common.base.Charsets;
import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import io.github.mac_genius.npcmail.NPCMail;
import io.github.mac_genius.npcmail.utils.CustProfileCallback;
import io.github.mac_genius.npcmail.utils.CustomPlayerConnection;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerJoinEvent;

import java.lang.reflect.Field;
import java.util.UUID;

/**
 * Created by Mac on 3/27/2016.
 */
public class MailMan {
    public static CraftPlayer createMan() {
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getHandle().getServer();
        String[] args = {"Windows_Genius"};
        CustProfileCallback callback = new CustProfileCallback();
        server.getGameProfileRepository().findProfilesByNames(args, new Agent("minecraft", 1), callback);
        if (callback.didFetchWorked()) {
            server.aD().fillProfileProperties(callback.getProfile(), true);
            for (Entity e : server.getWorld().entityList) {
                if (e.getUniqueID().equals(UUID.nameUUIDFromBytes(("OfflinePlayerFake:Windows_Genius").getBytes(Charsets.UTF_8)))) {
                    if (e instanceof EntityPlayer) {
                        server.getPlayerList().disconnect((EntityPlayer) e);
                    }
                    server.getWorld().removeEntity(e);
                }
            }
            changeProfile(callback.getProfile(), ChatColor.GOLD + "" + ChatColor.BOLD + "Right Click!");
            EntityPlayer mailMan = new EntityPlayer(server, server.getWorldServer(0), callback.getProfile(), new PlayerInteractManager(server.getWorld()));
            setInvulnerable(mailMan);
            CustomPlayerConnection.swapConnection(mailMan);
            mailMan.setLocation(NPCMail.getSingleton().getConfig().getDouble("location.x"), NPCMail.getSingleton().getConfig().getDouble("location.y"), NPCMail.getSingleton().getConfig().getDouble("location.z"), (float) NPCMail.getSingleton().getConfig().getDouble("location.yaw"), (float) NPCMail.getSingleton().getConfig().getDouble("location.pitch"));
            server.getWorld().addEntity(mailMan);
            CraftPlayer player = new CraftPlayer(server.server, mailMan);
            setHeldItem(mailMan, Item.getById(54), 1);
            spawnNames(mailMan);
            return player;
        } else {
            return null;
        }
    }

    public static void setInvulnerable(EntityPlayer player) {
        try {
            Class playerChange = ((Entity)player.f()).getClass();
            Field invuln = playerChange.getDeclaredField("invulnerable");
            invuln.setAccessible(true);
            invuln.set(player, true);
            invuln.setAccessible(false);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            if (e instanceof NoSuchFieldException) {
                NPCMail.getSingleton().getLogger().warning("Failed to find the invincible tag!");
            } else {
                NPCMail.getSingleton().getLogger().warning("Did not have access to the player!");
            }
        }
    }

    public static void setHeldItem(EntityPlayer player, Item item, int i) {
        player.playerConnection.sendPacket(new PacketPlayOutSetSlot(0, 36, new ItemStack(item, i)));
        PacketPlayInHeldItemSlot slot = new PacketPlayInHeldItemSlot();
        try {
            Field f = slot.getClass().getDeclaredField("itemInHandIndex");
            f.setAccessible(true);
            f.set(slot, 0);
            f.setAccessible(false);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            NPCMail.getSingleton().getLogger().warning("Failed to set the slot!");
        }
        player.playerConnection.a(slot);
    }

    public static void changeProfile(GameProfile profile, String name) {
        UUID fakePlayer = UUID.nameUUIDFromBytes(("OfflinePlayerFake:" + profile.getName()).getBytes(Charsets.UTF_8));

        try {
            Class playerChange = profile.getClass();
            Field playerName = playerChange.getDeclaredField("name");
            playerName.setAccessible(true);
            playerName.set(profile, name);
            playerName.setAccessible(false);

            Field uuid = playerChange.getDeclaredField("id");
            uuid.setAccessible(true);
            uuid.set(profile, fakePlayer);
            uuid.setAccessible(false);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            NPCMail.getSingleton().getLogger().warning("Failed to change the player profile!");
        }
    }

    public static void spawnNames(EntityPlayer player) {
        ArmorStand armorStand = (ArmorStand) Bukkit.getServer().getWorld("world").spawnEntity(new Location(Bukkit.getServer().getWorld("world"), player.locX, player.locY + .1, player.locZ), EntityType.ARMOR_STAND);
        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.setBasePlate(false);
        armorStand.setCustomName(ChatColor.BOLD + "Charles the Mail Man");
        armorStand.setCustomNameVisible(true);
        NPCMail.getSingleton().getArmorStands().add(armorStand);

        ArmorStand armorStand1 = (ArmorStand) Bukkit.getServer().getWorld("world").spawnEntity(new Location(Bukkit.getServer().getWorld("world"), player.locX, player.locY + .4, player.locZ), EntityType.ARMOR_STAND);
        armorStand1.setVisible(false);
        armorStand1.setGravity(false);
        armorStand1.setBasePlate(false);
        armorStand1.setCustomName("{mail}");
        armorStand1.setCustomNameVisible(true);
        NPCMail.getSingleton().getArmorStands().add(armorStand1);
    }
}
