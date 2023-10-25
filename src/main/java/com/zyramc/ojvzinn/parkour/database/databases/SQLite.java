package com.zyramc.ojvzinn.parkour.database.databases;

import com.zyramc.ojvzinn.parkour.Main;
import com.zyramc.ojvzinn.parkour.database.DataBase;
import com.zyramc.ojvzinn.parkour.database.interfaces.DatabaseInterface;
import com.zyramc.ojvzinn.parkour.utils.FileUtils;
import org.bukkit.Bukkit;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.sql.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class SQLite extends DataBase implements DatabaseInterface<SQLite> {

    private final Main main;
    public Connection connection;

    public SQLite(Main main) {
        this.main = main;
    }

    @Override
    public void setupDataBase() {
        try {
            getConnection();
            main.sendMessage("A conexão com o SQLite foi executada com sucesso!", "6");
            closeConnection();
            this.connection = null;
        } catch (Exception ex) {
            ex.printStackTrace();
            closeConnection();
            System.exit(0);
            Bukkit.shutdown();
            main.sendMessage("A conexão com o SQLIte não foi bem sucedida!", "c");
        }
    }

    @Override
    public Connection getConnection() {
        if (this.connection == null) {
            try {
                FileUtils fileUtils = new FileUtils("profiles", main.getDescription().getName(), SQLite.class);

                File file = fileUtils.loadFileYamlTermination("db");

                String URL = "jdbc:sqlite:" + file;
                Class.forName("org.sqlite.JDBC");
                return DriverManager.getConnection(URL);
            } catch (Exception ex) {
                ex.printStackTrace();
                closeConnection();
                System.exit(0);
            }
        }

        return connection;
    }

    @Override
    public void createTable(String table) {
        try {
            Statement statement = getConnection().createStatement();
            statement.execute(table);
            statement.close();
            closeConnection();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void closeConnection() {
        try {
            if (this.connection != null) {
                this.connection.close();
                this.connection = null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Main getMain() {
        return main;
    }

    public void createDefaultTables() {
        createTable("CREATE TABLE IF NOT EXISTS ProfileTimer (`NAME` TEXT, `TIME` TEXT)");
    }

    public void executeSQL(String SQLExecute) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(SQLExecute);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void updateStatusPlayer(String player, String table, String column, String value) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("UPDATE " + table + " SET " + column.toUpperCase(Locale.ROOT) + " = '" + value + "' WHERE NAME = '" + player.toLowerCase(Locale.ROOT) + "'");
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void updateStatusPlayer(String player, String table, String column, Integer value) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("UPDATE " + table + " SET " + column.toUpperCase(Locale.ROOT) + " = '" + value + "' WHERE NAME = '" + player.toLowerCase(Locale.ROOT) + "'");
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void updateStatusPlayer(String player, String table, String column, Long value) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("UPDATE " + table + " SET " + column.toUpperCase(Locale.ROOT) + " = '" + value + "' WHERE NAME = '" + player.toLowerCase(Locale.ROOT) + "'");
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void deleteColumn(String player, String table, String column, String value) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("DROP FROM " + table + " WHERE " + column.toUpperCase(Locale.ROOT) + " = '" + value + "'");
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getStatusForPlayerString(String player, String coluna, String table) {
        try {
            Statement statement = getConnection().createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM " + table + " WHERE NAME = '" + player.toLowerCase(Locale.ROOT) + "'");
            String result = "";
            while (rs.next()) {
                result = rs.getString(coluna);
            }
            rs.close();
            statement.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getStatusForPlayerObject(String player, String coluna, String table) {
        try {
            Statement statement = getConnection().createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM " + table + " WHERE NAME = '" + player.toLowerCase(Locale.ROOT) + "'");
            JSONObject result = null;
            while (rs.next()) {
                JSONParser parser = new JSONParser();
                result = (JSONObject) parser.parse(rs.getString(coluna));
            }
            rs.close();
            statement.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Integer getStatusForPlayerInterger(String player, String coluna, String table) {
        try {
            Statement statement = getConnection().createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM " + table + " WHERE NAME = '" + player.toLowerCase(Locale.ROOT) + "'");
            int result = 0;
            while (rs.next()) {
                result = rs.getInt(coluna);
            }
            rs.close();
            statement.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Long getStatusForPlayerLong(String player, String coluna, String table) {
        try {
            Statement statement = getConnection().createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM " + table + " WHERE NAME = '" + player.toLowerCase(Locale.ROOT) + "'");
            long result = 0;
            while (rs.next()) {
                result = rs.getLong(coluna);
            }
            rs.close();
            statement.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addStatusDefaultPlayer(String player, String table) {
        try {
            if (table.equalsIgnoreCase("ProfileTimer")) {
                PreparedStatement preparedStatement = getConnection().prepareStatement("INSERT INTO " + table + " values(?,?)");
                preparedStatement.setString(1, player.toLowerCase(Locale.ROOT));
                preparedStatement.setString(2, "");
                preparedStatement.executeUpdate();
                preparedStatement.close();
                preparedStatement.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean conteinsPlayer(String player, String table) {
        try {
            Statement statement = getConnection().createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM " + table);

            while (rs.next()) {
                if (rs.getString("NAME").equals(player.toLowerCase(Locale.ROOT))) {
                    statement.close();
                    rs.close();
                    return true;
                }
            }
            statement.close();
            rs.close();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Map<String, Long> getListPlayers(String table) {
        try {
            Statement statement = getConnection().createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM " + table);
            Map<String, Long> a =new HashMap<>();
            while (rs.next()) {
                if (!Objects.equals(rs.getString("TIME"), "")) {
                    a.put(rs.getString("NAME"), Long.parseLong(rs.getString("TIME")));
                }
            }
            statement.close();
            rs.close();
            return a;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
