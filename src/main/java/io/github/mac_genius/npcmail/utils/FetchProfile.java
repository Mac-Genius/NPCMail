package io.github.mac_genius.npcmail.utils;

import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_9_R1.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_9_R1.CraftServer;

/**
 * Created by Mac on 3/28/2016.
 */
public class FetchProfile {
    public static GameProfile lookup(String name) {
        MinecraftServer server = ((CraftServer)Bukkit.getServer()).getServer();
        CustProfileCallback callback = new CustProfileCallback();
        server.getGameProfileRepository().findProfilesByNames(new String[]{name}, new Agent("minecraft", 1), callback);
        if (callback.didFetchWorked()) {
            return callback.getProfile();
        } else {
            return null;
        }
    }
}
