package net.snapecraft.spigotvote;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.UUID;

public final class SpigotVote extends JavaPlugin implements Listener {

    Connection connection;
    boolean mysqlInit = false;
    static SpigotVote instance;

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginCommand("voteshop").setExecutor(this);

        initConfig();
        try {
            initMysql();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    private void initConfig() {
        saveConfig();
        reloadConfig();
        getConfig().addDefault("SQL.host", "localhost");
        getConfig().addDefault("SQL.user", "root");
        getConfig().addDefault("SQL.pw", "");
        getConfig().addDefault("SQL.db", "vote");
        getConfig().addDefault("SQL.port", 3306);

        getConfig().addDefault("Shopconfig.size", 4);

        getConfig().addDefault("Shop.0.name", "Diamand");
        getConfig().addDefault("Shop.0.material", "DIAMOND");
        getConfig().addDefault("Shop.0.price", 500);

        getConfig().addDefault("Shop.1.name", "Eisen");
        getConfig().addDefault("Shop.1.material", "IRON_INGOT");
        getConfig().addDefault("Shop.1.price", 100);
        getConfig().options().copyDefaults(true);
        saveConfig();
        reloadConfig();
    }

    private void initMysql() throws ClassNotFoundException, SQLException {
        String host = getConfig().getString("SQL.host");
        String user = getConfig().getString("SQL.user");
        String pw = getConfig().getString("SQL.pw");
        String db = getConfig().getString("SQL.db");
        int port = getConfig().getInt("SQL.port");
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://" + host+ ":" + port + "/" + db, user, pw);
        if(connection.isClosed()) {
            System.out.println("[VOTE] MYSQL ERROR");
        }
        boolean mysqlReady = !connection.isClosed();
        if(mysqlReady) {
            Statement sqlst = connection.createStatement();

            String sql1 = "CREATE TABLE IF NOT EXISTS `vote_count`" +
                    "( `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    " `uuid` VARCHAR(40) NOT NULL UNIQUE, " +
                    "`votecount` INT NOT NULL , " +
                    "`lastvote` DATE NOT NULL);";
            sqlst.executeUpdate(sql1);

            String sql2 = "CREATE TABLE IF NOT EXISTS `vote_points`" +
                    "( `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    " `uuid` VARCHAR(40) NOT NULL UNIQUE, " +
                    "`valuecount` INT NOT NULL);";
            sqlst.executeUpdate(sql2);

            sqlst.close();
        }
        mysqlInit = true;
    }

    private boolean isReady() {
        if(!mysqlInit) {
            return false;
        }
        try {
            if(!connection.isClosed()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length != 0) {
            sender.sendMessage("§cWrong Usage! /voteshop");
            return true;
        }
        Player p = (Player) sender;
        if(isReady()) {
            Gui.openshop(p);
        } else {
            sender.sendMessage("§cMysql wurde nicht geladen! Kontaktiere ein Teammitglied.");
        }


        return true;
    }


    public VoteObject getVotes(Player p) {
        if(isReady()) {
            try {
                VoteObject obj = null;
                String sql = "SELECT * FROM vote_count WHERE uuid = '%s'";
                String sql2 = "SELECT valuecount FROM vote_points WHERE uuid = '%s'";
                sql = String.format(sql, getUUID(p.getUniqueId()));
                sql2 = String.format(sql2, getUUID(p.getUniqueId()));
                Statement stmt = connection.createStatement();
                Statement stmt2 = connection.createStatement();

                ResultSet pset = stmt.executeQuery(sql2);
                int points = 0;
                while (pset.next()) {
                    points = pset.getInt("valuecount");
                }
                pset.close();
                stmt.close();

                ResultSet rs = stmt2.executeQuery(sql);
                while (rs.next()) {
                    obj = new VoteObject(
                            getUUID(p.getUniqueId()),
                            rs.getInt("votecount"),
                            points, rs.getDate("lastvote")
                    );
                }
                rs.close();
                stmt2.close();
                return obj;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public void setVoteCoins(Player p, int coins) {
        try {
            String sql = "UPDATE vote_points SET valuecount = " + coins + " WHERE uuid = '" + getUUID(p.getUniqueId()) + "';";
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @param id Spigots UUID Object
     * @return Player short UUID
     */
    private String getUUID(UUID id) {
        return id.toString().replaceAll("-", "");
    }

    public static SpigotVote getInstance() {
        return instance;
    }


}
