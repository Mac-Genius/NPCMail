package io.github.mac_genius.npcmail;

import io.github.mac_genius.npcmail.commands.NPCMail_Command;
import io.github.mac_genius.npcmail.database.SQLConnect;
import io.github.mac_genius.npcmail.database.cache.ServerMail;
import io.github.mac_genius.npcmail.listeners.PlayerListeners;
import io.github.mac_genius.npcmail.npc.MailMan;
import io.github.mac_genius.npcmail.tasks.ShowPlayer;
import io.github.mac_genius.npcmail.tasks.UpdateNPCName;
import io.github.mac_genius.sqleconomy.economy.Economy;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

/**
 * Created by Mac on 3/26/2016.
 */
public class NPCMail extends JavaPlugin {
    private static NPCMail singleton;
    private CraftPlayer mailMan;
    private ArrayList<ArmorStand> armorStands;
    private SQLConnect connect;
    private ServerMail mail;
    private Economy economy;

    @Override
    public void onEnable() {
        singleton = this;
        armorStands = new ArrayList<>();
        mailMan = MailMan.createMan();
        connect = new SQLConnect();
        saveDefaultConfig();
        if (connect.testConnection()) {
            if (setupEconomy()) {
                mail = new ServerMail();
                getCommand("npcmail").setExecutor(new NPCMail_Command());
                getServer().getScheduler().runTaskTimer(this, new UpdateNPCName(), 0, 10);
                getServer().getScheduler().runTaskTimer(this, new ShowPlayer(), 0, 10);
                getServer().getPluginManager().registerEvents(new PlayerListeners(), this);
                getLogger().info("Plugin enabled!");
            } else {
                getLogger().warning("Failed to setup the economy!");
                getServer().getPluginManager().disablePlugin(this);
            }
        } else {
            getLogger().warning("Failed to connect to the MySQL database!");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        mailMan.getHandle().server.getPlayerList().players.remove(mailMan.getHandle());
        for (ArmorStand a : armorStands) {
            a.remove();
        }
        getLogger().info("Plugin disabled!");
    }

    public static NPCMail getSingleton() {
        return singleton;
    }

    public CraftPlayer getMailMan() {
        return mailMan;
    }

    public ArrayList<ArmorStand> getArmorStands() {
        return armorStands;
    }

    public SQLConnect getConnect() {
        return connect;
    }

    public ServerMail getMail() {
        return mail;
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
            return true;
        }
        return false;
    }

    public Economy getEconomy() {
        return economy;
    }
}
