package io.github.mac_genius.npcmail.database.cache;

import com.mojang.authlib.GameProfile;
import io.github.mac_genius.npcmail.NPCMail;
import io.github.mac_genius.npcmail.database.models.Mail;
import io.github.mac_genius.npcmail.database.models.Survey;
import io.github.mac_genius.npcmail.database.models.Vote;
import io.github.mac_genius.npcmail.utils.FetchProfile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Mac on 3/28/2016.
 */
public class ServerMail {
    private ArrayList<Mail> allActiveMail;
    private HashMap<Player, PlayerMail> playerMail;

    public ServerMail() {
        allActiveMail = new ArrayList<>();
        playerMail = new HashMap<>();
        loadMail();
    }

    public void addMail(Mail newMail) {
        if (!allActiveMail.contains(newMail)) {
            allActiveMail.add(newMail);
            for (Player m : playerMail.keySet()) {
                playerMail.get(m).addMail(newMail);
                NPCMail.getSingleton().getServer().getScheduler().runTaskAsynchronously(NPCMail.getSingleton(), () -> {
                    addPlayerMailDatabase(m.getUniqueId(), newMail.getName(), 0);
                });
            }
            addMailDatabase(newMail);
        }
    }

    public Mail removeMail(String name) {
        for (Mail m : new ArrayList<>(allActiveMail)) {
            if (m.getName().equalsIgnoreCase(name)) {
                allActiveMail.remove(m);
                NPCMail.getSingleton().getServer().getScheduler().runTaskAsynchronously(NPCMail.getSingleton(), () -> {
                    for (Player p : playerMail.keySet()) {
                        removePlayerMailDatabase(p.getUniqueId(), m.getName());
                        playerMail.get(p).removeMail(m.getName());
                    }
                    retireMailDatabase(m);
                });
                return m;
            }
        }
        return null;
    }

    public Mail getMail(String name, boolean isDisplayName) {
        if (isDisplayName) {
            for (Mail m : allActiveMail) {
                if (m.getDisplayName().equals(name)) {
                    return m;
                }
            }
            return null;
        } else {
            for (Mail m : allActiveMail) {
                if (m.getName().equalsIgnoreCase(name)) {
                    return m;
                }
            }
            return null;
        }
    }

    public void loadMail() {
        NPCMail.getSingleton().getLogger().info("Loading all mail...");
        ArrayList<Mail> allMail = getAllMailDatabase();
        if (allMail != null) {
            for (Mail m : allMail) {
                allActiveMail.add(m);
            }
            NPCMail.getSingleton().getLogger().info("Done.");
        }
    }

    public ArrayList<Mail> getAllActiveMail() {
        return allActiveMail;
    }

    public void updateMail(Mail mail) {
        for (Mail m : allActiveMail) {
            if (m.getName().equalsIgnoreCase(mail.getName())) {
                m.updateMail(mail);
                updateMailDatabase(mail);
                break;
            }
        }
    }

    public PlayerMail getPlayerMail(Player p) {
        return playerMail.get(p);
    }

    public PlayerMail getPlayerMail(String name) {
        Player p = NPCMail.getSingleton().getServer().getPlayer(name);
        if (p != null) {
            return playerMail.get(p);
        }
        return null;
    }

    public PlayerMail getPlayerMail(UUID uuid) {
        Player p = NPCMail.getSingleton().getServer().getPlayer(uuid);
        if (p != null) {
            return playerMail.get(p);
        }
        return null;
    }

    public PlayerMail loadPlayer(Player p) {
        PlayerMail mail = getPlayerMailDatabase(p);
        for (Mail m : allActiveMail) {
            if (!mail.hasMail(m)) {
                mail.addMail(m);
                NPCMail.getSingleton().getServer().getScheduler().runTaskAsynchronously(NPCMail.getSingleton(), () ->{
                    addPlayerMailDatabase(p.getUniqueId(), m.getName(), 0);
                });

            }
        }
        playerMail.put(p, mail);
        return mail;
    }

    public PlayerMail unloadPlayer(Player p) {
        PlayerMail mail = playerMail.get(p);
        playerMail.remove(p);
        return mail;
    }

    public void updatePlayerMail(Player player, String name, boolean isDisplayName, long lastClicked) {
        PlayerMail mail = playerMail.get(player);
        Mail mail1 = getMail(name, isDisplayName);
        mail.addMail(name, lastClicked);
        NPCMail.getSingleton().getServer().getScheduler().runTaskAsynchronously(NPCMail.getSingleton(), () -> {
            updatePlayerMailDatabase(player.getUniqueId(), mail1.getName(), lastClicked);
        });
    }

    public void updatePlayerMail(String playerName, String mailName, boolean isDisplayName, long lastClicked) {
        GameProfile profile = FetchProfile.lookup(playerName);
        if (profile != null) {
            UUID uuid = profile.getId();
            Mail mail1 = getMail(mailName, isDisplayName);
            updatePlayerMailDatabase(uuid, mail1.getName(), lastClicked);
        }
    }

    // MySQL Functions
    private void addMailDatabase(Mail mail) {
        Connection connection = NPCMail.getSingleton().getConnect().getConnection();
        if (connection != null) {
            try {
                PreparedStatement statement = connection.prepareStatement("INSERT INTO NPCMail_Mail_Active (Name, Type, Expire, Refresh, DisplayName, Lore, Slot, FinishMessage, AmountWorth) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                statement.setString(1, mail.getName());
                statement.setString(2, mail.getType());
                statement.setLong(3, mail.getExpire());
                statement.setLong(4, mail.getRefresh());
                statement.setString(5, mail.getDisplayName());
                statement.setString(6, mail.getLoreString());
                statement.setInt(7, mail.getSlot());
                statement.setString(8, mail.getDoneMessage());
                statement.setInt(9, mail.getWorth());
                statement.executeUpdate();
                statement.close();

                if (mail instanceof Vote) {
                    statement = connection.prepareStatement("INSERT INTO NPCMail_Vote_Active (Name, Link) VALUES (?, ?)");
                    statement.setString(1, mail.getName());
                    statement.setString(2, ((Vote) mail).getLink());
                    statement.executeUpdate();
                    statement.close();
                }

                if (mail instanceof Survey) {
                    statement = connection.prepareStatement("INSERT INTO NPCMail_SurveyList_Active (Name, Type, Question, Answers) VALUES (?, ?, ?, ?)");
                    statement.setString(1, mail.getName());
                    statement.setString(2, ((Survey) mail).getSurveyType());
                    statement.setString(3, ((Survey) mail).getQuestion());
                    statement.setString(4, ((Survey) mail).getAnswerString());
                    statement.executeUpdate();
                    statement.close();
                }
                connection.close();
            } catch (SQLException e) {
                NPCMail.getSingleton().getLogger().info(e.getMessage());
                NPCMail.getSingleton().getLogger().info("Failed to add mail.");
            }
        } else {
            NPCMail.getSingleton().getLogger().info("Failed to add mail.");
        }
    }

    private Mail getMailDatabase(String name) {
        Connection connection = NPCMail.getSingleton().getConnect().getConnection();
        if (connection != null) {
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT NPCMail_Mail_Active.*, NPCMail_Vote_Active.Link, NPCMail_Survey_Active.Type, NPCMail_Survey_Active.Question, NPCMail_Survey_Active.Answers FROM NPCMail_Mail_Active INNER JOIN (NPCMail_Vote_Active, NPCMail_Survey_Active) ON NPCMail_Mail_Active.Name=? AND (NPCMail_Mail_Active.Name=NPCMail_Vote_Active OR NPCMail_Mail_Active.Name=NPCMail_SurveyList_Active)");
                statement.setString(1, name);
                ResultSet set = statement.executeQuery();
                Mail mail = null;
                while (set.next()) {
                    String type = set.getString(3);
                    if (type.equalsIgnoreCase("vote")) {
                        mail = new Vote(set.getString(2), set.getString(3), set.getString(6), set.getString(9), set.getString(11), set.getString(7), set.getLong(4), set.getLong(5), set.getInt(8), set.getInt(10));
                    } else if (type.equalsIgnoreCase("survey")) {
                        mail = new Survey(set.getString(2), set.getString(3), set.getString(6), set.getString(9), set.getString(11), set.getString(12), set.getString(13), set.getString(7), set.getLong(4), set.getLong(5), set.getInt(8), set.getInt(10));
                    } else {
                        mail = new Mail(set.getString(2), set.getString(3), set.getString(6), set.getString(9), set.getString(7), set.getLong(4), set.getLong(5), set.getInt(8), set.getInt(10));
                    }
                }
                set.close();
                statement.close();
                connection.close();
                if (mail != null) {
                    return mail;
                }
            } catch (SQLException e) {
                NPCMail.getSingleton().getLogger().info(e.getMessage());
                NPCMail.getSingleton().getLogger().info("Failed to fetch mail for " + name + "!");
            }
        } else {
            NPCMail.getSingleton().getLogger().info("Failed to fetch mail for " + name + "!");
        }
        return null;
    }

    private ArrayList<Mail> getAllMailDatabase() {
        Connection connection = NPCMail.getSingleton().getConnect().getConnection();
        if (connection != null) {
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT NPCMail_Mail_Active.*, NPCMail_Vote_Active.Link, NPCMail_SurveyList_Active.Type, NPCMail_SurveyList_Active.Question, NPCMail_SurveyList_Active.Answers FROM NPCMail_Mail_Active LEFT JOIN (NPCMail_Vote_Active, NPCMail_SurveyList_Active) ON (NPCMail_Mail_Active.Name=NPCMail_Vote_Active.Name AND NPCMail_Mail_Active.Name=NPCMail_SurveyList_Active.Name)");
                ResultSet set = statement.executeQuery();
                ArrayList<Mail> mail = new ArrayList<>();
                while (set.next()) {
                    Mail newMail = null;
                    String type = set.getString(3);
                    if (type.equalsIgnoreCase("vote")) {
                        newMail = new Vote(set.getString(2), set.getString(3), set.getString(6), set.getString(9), set.getString(11), set.getString(7), set.getLong(4), set.getLong(5), set.getInt(8), set.getInt(10));
                    } else if (type.equalsIgnoreCase("survey")) {
                        newMail = new Survey(set.getString(2), set.getString(3), set.getString(6), set.getString(9), set.getString(12), set.getString(13), set.getString(14), set.getString(7), set.getLong(4), set.getLong(5), set.getInt(8), set.getInt(10));
                    } else {
                        newMail = new Mail(set.getString(2), set.getString(3), set.getString(6), set.getString(9), set.getString(7), set.getLong(4), set.getLong(5), set.getInt(8), set.getInt(10));
                    }
                    mail.add(newMail);
                }
                set.close();
                statement.close();
                connection.close();
                return mail;
            } catch(SQLException e) {
                NPCMail.getSingleton().getLogger().info(e.getMessage());
                NPCMail.getSingleton().getLogger().info("Failed to fetch all mail!");
            }
        } else {
            NPCMail.getSingleton().getLogger().info("Failed to fetch all mail!");
        }
        return null;
    }

    private void updateMailDatabase(Mail mail) {
        NPCMail.getSingleton().getLogger().info("Updating mail: " + mail.getName());
        Connection connection = NPCMail.getSingleton().getConnect().getConnection();
        if (connection != null) {
            try {
                PreparedStatement statement = connection.prepareStatement("UPDATE NPCMail_Mail_Active SET Expire=?, Refresh=?, DisplayName=?, Lore=?, Slot=?, FinishMessage=?, AmountWorth=? WHERE Name=?");
                statement.setLong(1, mail.getExpire());
                statement.setLong(2, mail.getRefresh());
                statement.setString(3, mail.getDisplayName());
                statement.setString(4, mail.getLoreString());
                statement.setInt(5, mail.getSlot());
                statement.setString(6, mail.getDoneMessage());
                statement.setInt(7, mail.getWorth());
                statement.setString(8, mail.getName());
                statement.executeUpdate();
                statement.close();

                if (mail instanceof Vote) {
                    statement = connection.prepareStatement("UPDATE NPCMail_Vote_Active SET Link=? WHERE Name=?");
                    statement.setString(1, ((Vote) mail).getLink());
                    statement.setString(2, mail.getName());
                    statement.executeUpdate();
                    statement.close();
                }

                if (mail instanceof Survey) {
                    statement = connection.prepareStatement("UPDATE NPCMail_SurveyList_Active SET Question=?, Answers=? WHERE Name=?");
                    statement.setString(1, ((Survey) mail).getQuestion());
                    statement.setString(2, ((Survey) mail).getAnswerString());
                    statement.setString(3, mail.getName());
                    statement.executeUpdate();
                    statement.close();
                }
            } catch (SQLException e) {
                NPCMail.getSingleton().getLogger().info(e.getMessage());
                NPCMail.getSingleton().getLogger().info("Failed to update mail for " + mail.getName() + "!");
            }
        }
    }

    private void retireMailDatabase(Mail mail) {
        Connection connection = NPCMail.getSingleton().getConnect().getConnection();
        if (connection != null) {
            try {
                PreparedStatement statement = connection.prepareStatement("INSERT INTO NPCMail_Mail_Old SELECT * FROM NPCMail_Mail_Active WHERE 'Name'=?");
                statement.setString(1, mail.getName());
                statement.executeUpdate();
                statement.close();

                statement = connection.prepareStatement("DELETE FROM NPCMail_Mail_Active WHERE 'Name'=?");
                statement.setString(1, mail.getName());
                statement.executeUpdate();
                statement.close();

                if (mail instanceof Vote) {
                    statement = connection.prepareStatement("INSERT INTO NPCMail_Vote_Old SELECT * FROM NPCMail_Vote_Active WHERE 'Name'=?");
                    statement.setString(1, mail.getName());
                    statement.executeUpdate();
                    statement.close();

                    statement = connection.prepareStatement("DELETE FROM NPCMail_Vote_Active WHERE 'Name'=?");
                    statement.setString(1, mail.getName());
                    statement.executeUpdate();
                    statement.close();
                }

                if (mail instanceof Survey) {
                    statement = connection.prepareStatement("INSERT INTO NPCMail_SurveyList_Old SELECT * FROM NPCMail_SurveyList_Active WHERE 'Name'=?");
                    statement.setString(1, mail.getName());
                    statement.executeUpdate();
                    statement.close();

                    statement = connection.prepareStatement("DELETE FROM NPCMail_SurveyList_Active WHERE 'Name'=?");
                    statement.setString(1, mail.getName());
                    statement.executeUpdate();
                    statement.close();
                }
            } catch (SQLException e) {
                NPCMail.getSingleton().getLogger().info(e.getMessage());
                NPCMail.getSingleton().getLogger().info("Failed to update mail for " + mail.getName() + "!");
            }
        }
    }

    private PlayerMail getPlayerMailDatabase(String name) {
        Player p = Bukkit.getPlayer(name);
        if (p != null) {
            return getPlayerMailDatabase(p.getUniqueId());
        } else {
            GameProfile profile = FetchProfile.lookup(name);
            if (profile != null) {
                return getPlayerMailDatabase(profile.getId());
            }
        }
        return null;
    }

    private PlayerMail getPlayerMailDatabase(UUID uuid) {
        Connection connection = NPCMail.getSingleton().getConnect().getConnection();
        if (connection != null) {
            try {
                PlayerMail mail = new PlayerMail();
                PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXIStS NPCMail_Player_" + uuid.toString().replaceAll("-", "_") + "(MailName VARCHAR(25), LastSelected BIGINT DEFAULT 0)");
                statement.executeUpdate();
                statement.close();

                statement = connection.prepareStatement("Select * FROM NPCMail_Player_" + uuid.toString().replaceAll("-", "_"));
                ResultSet results = statement.executeQuery();
                while (results.next()) {
                    mail.addMail(results.getString(1), results.getLong(2));
                }
                results.close();
                statement.close();
                connection.close();
                return mail;
            } catch (SQLException e) {
                NPCMail.getSingleton().getLogger().info(e.getMessage());
                NPCMail.getSingleton().getLogger().info(e.getMessage());
            }
        }
        NPCMail.getSingleton().getLogger().info("Failed to fetch the player mail for " + uuid.toString().replaceAll("-", "_") + "!");
        return null;
    }

    private PlayerMail getPlayerMailDatabase(Player player) {
        return getPlayerMailDatabase(player.getUniqueId());
    }

    private void updatePlayerMailDatabase(UUID playerUUID, String mailName, long lastClicked) {
        Connection connection = NPCMail.getSingleton().getConnect().getConnection();
        if (connection != null) {
            try {
                PreparedStatement statement = connection.prepareStatement("UPDATE NPCMail_Player_" + playerUUID.toString().replaceAll("-", "_") + " SET LastSelected=? WHERE MailName=?");
                statement.setLong(1, lastClicked);
                statement.setString(2, mailName);
                statement.executeUpdate();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                NPCMail.getSingleton().getLogger().info(e.getMessage());
                NPCMail.getSingleton().getLogger().info("Failed to update player info for " + playerUUID.toString() + "!");
            }
        } else {
            NPCMail.getSingleton().getLogger().info("Failed to update player info for " + playerUUID.toString() + "!");
        }
    }

    private void addPlayerMailDatabase(UUID player, String mailName, long lastClicked) {
        Connection connection = NPCMail.getSingleton().getConnect().getConnection();
        if (connection != null) {
            try {
                PreparedStatement statement = connection.prepareStatement("INSERT INTO NPCMail_Player_" + player.toString().replaceAll("-", "_") + " (MailName, LastSelected) VALUES (?, ?)");
                statement.setString(1, mailName);
                statement.setLong(2, lastClicked);
                statement.executeUpdate();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                NPCMail.getSingleton().getLogger().info(e.getMessage());
                NPCMail.getSingleton().getLogger().info("Failed to add player info for " + player.toString() + "!");
            }
        } else {
            NPCMail.getSingleton().getLogger().info("Failed to add player info for " + player.toString() + "!");
        }
    }

    private void removePlayerMailDatabase(UUID player, String mailName) {
        Connection connection = NPCMail.getSingleton().getConnect().getConnection();
        if (connection != null) {
            try {
                PreparedStatement statement = connection.prepareStatement("DELETE FROM NPCMail_Player_" + player.toString().replaceAll("-", "_") + " WHERE MailName=?");
                statement.setString(1, mailName);
                statement.executeUpdate();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                NPCMail.getSingleton().getLogger().info(e.getMessage());
                NPCMail.getSingleton().getLogger().info("Failed to remove player info for " + player.toString() + "!");
            }
        } else {
            NPCMail.getSingleton().getLogger().info("Failed to remove player info for " + player.toString() + "!");
        }
    }
}
