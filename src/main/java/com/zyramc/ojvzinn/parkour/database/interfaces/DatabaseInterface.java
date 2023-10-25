package com.zyramc.ojvzinn.parkour.database.interfaces;

import com.zyramc.ojvzinn.parkour.database.DataBase;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseInterface<T extends DataBase> {
    void setupDataBase();
    Connection getConnection() throws ClassNotFoundException, SQLException;
    void createTable(String table);
    void closeConnection();
}
