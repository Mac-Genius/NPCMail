package io.github.mac_genius.npcmail.database;

import io.github.mac_genius.npcmail.NPCMail;
import org.bukkit.Bukkit;
import org.fusesource.jansi.Ansi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Mac on 3/28/2016.
 */
public class SQLConnect {
    public SQLConnect() {
        databaseSetup();
    }

    public void databaseSetup() {
        Connection connection = getConnection();
        if (connection != null) {
            for (String s : getTables()) {
                try {
                    PreparedStatement tokoins = connection.prepareStatement("CREATE TABLE IF NOT EXISTS NPCMail_" + s);
                    tokoins.executeUpdate();
                    tokoins.close();
                } catch (SQLException e) {
                    NPCMail.getSingleton().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Could not create the " + s.substring(0, s.indexOf("(")) + " table." + Ansi.ansi().fg(Ansi.Color.WHITE));
                    NPCMail.getSingleton().getLogger().info(e.getMessage());
                }
            }
        }
    }

    public Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            NPCMail.getSingleton().getLogger().warning(Ansi.ansi().fg(Ansi.Color.RED) + "Could not find the connection driver. Make sure it is installed and try again." + Ansi.ansi().fg(Ansi.Color.WHITE));
            return null;
        }
        Connection con = null;

        String url = "jdbc:mysql://" + NPCMail.getSingleton().getConfig().getString("ip") + ":" + NPCMail.getSingleton().getConfig().getString("port") + "/" + NPCMail.getSingleton().getConfig().getString("database");
        String user = NPCMail.getSingleton().getConfig().getString("user");
        String password = NPCMail.getSingleton().getConfig().getString("password");

        try {
            con = DriverManager.getConnection(url, user, password);

        } catch (SQLException ex) {
            Bukkit.getLogger().warning("Error brah");

        }
        return con;
    }

    public boolean testConnection() {
        Connection connection = getConnection();
        boolean canConnect = connection != null;
        if (canConnect) {
            try {
                connection.close();
            } catch (SQLException e) {
            }
        }
        return canConnect;
    }

    public ArrayList<String> getTables() {
        ArrayList<String> tables = new ArrayList<>();
        tables.add("Mail_Active(Id INT PRIMARY KEY AUTO_INCREMENT, Name VARCHAR(25), Type VARCHAR(10), Expire BIGINT DEFAULT 0, Refresh BIGINT DEFAULT 1200000, DisplayName VARCHAR(25) NOT NULL, Lore TEXT, Slot INT DEFAULT -1, FinishMessage TEXT, AmountWorth INT DEFAULT 0)");
        tables.add("Mail_Old(Id INT PRIMARY KEY AUTO_INCREMENT, Name VARCHAR(25), Type VARCHAR(10), Expire BIGINT DEFAULT 0, Refresh BIGINT DEFAULT 1200000, DisplayName VARCHAR(25) NOT NULL, Lore TEXT, Slot INT DEFAULT -1, FinishMessage TEXT, AmountWorth INT DEFAULT 0)");
        tables.add("Vote_Active(Id INT PRIMARY KEY AUTO_INCREMENT, Name VARCHAR(25), Link TEXT)");
        tables.add("Vote_Old(Id INT PRIMARY KEY AUTO_INCREMENT, Name VARCHAR(25), Link TEXT)");
        tables.add("SurveyList_Active(Id INT PrimARY KEY AUTO_INCREMENT, Name VARCHAR(25), Type VARCHAR(10), Question TEXT, Answers TEXT)");
        tables.add("SurveyList_Old(Id INT PrimARY KEY AUTO_INCREMENT, Name VARCHAR(25), Type VARCHAR(10), Question TEXT, Answers TEXT)");
        return tables;
    }
}
