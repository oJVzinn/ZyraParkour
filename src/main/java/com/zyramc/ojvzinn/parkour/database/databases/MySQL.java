package com.zyramc.ojvzinn.parkour.database.databases;

import com.zyramc.ojvzinn.parkour.Main;
import com.zyramc.ojvzinn.parkour.database.DataBase;
import com.zyramc.ojvzinn.parkour.database.interfaces.DatabaseInterface;
import org.bukkit.Bukkit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.sql.*;
import java.util.Locale;

public class MySQL extends DataBase implements DatabaseInterface<MySQL> {

    private final Main main;
    public Connection connection;

    public MySQL(Main main) {
        this.main = main;
    }

    @Override
    public void setupDataBase() {
        try {
            String host = main.getConfig().getString("database.mysql.host");
            String port = main.getConfig().getString("database.mysql.porta");
            String nome = main.getConfig().getString("database.mysql.nome");
            String usuario = main.getConfig().getString("database.mysql.usuario");
            String senha = main.getConfig().getString("database.mysql.senha");

            setupConnection(host, port, nome, usuario, senha);
            main.sendMessage("A conexão com o MySQL foi executada com sucesso!", "6");
            this.connection.close();
            this.connection = null;
            closeConnection();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            closeConnection();
            System.exit(0);
            Bukkit.shutdown();
            main.sendMessage("A conexão com o MySQL não foi bem sucedida!", "c");
        }
    }

    @Override
    public Connection getConnection() {
        if (this.connection == null) {
            String host = main.getConfig().getString("database.mysql.host");
            String port = main.getConfig().getString("database.mysql.porta");
            String nome = main.getConfig().getString("database.mysql.nome");
            String usuario = main.getConfig().getString("database.mysql.usuario");
            String senha = main.getConfig().getString("database.mysql.senha");
            try {
                setupConnection(host, port, nome, usuario, senha);
            } catch (Exception ex) {
                ex.printStackTrace();
                closeConnection();
                System.exit(0);
            }
        }
        return connection;
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

    public void setupConnection(String host, String port, String nome, String usuario, String senha) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        this.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + nome, usuario, senha);
    }

    public Main getMain() {
        return main;
    }

    public void createDefaultTables() {
        createTable("CREATE TABLE IF NOT EXISTS ProfileGeral (`NAME` VARCHAR (24) NOT NULL, `TAG` VARCHAR(24), `PREFERENCES` VARCHAR(300), `LEVEL` VARCHAR(64), `XP` VARCHAR(64), `FIRST_LOGIN` VARCHAR(24), `LAST_LOGIN` VARCHAR(24), `CASH` VARCHAR(24), `XP_REWARDS` VARCHAR(300))");
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

    public void deleteColumn(String table, String column, String value) {
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
            if (table.equalsIgnoreCase("ProfileGeral")) {
                PreparedStatement preparedStatement = getConnection().prepareStatement("INSERT INTO " + table + " values(?,?,?,?,?,?,?,?,?)");
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
}
