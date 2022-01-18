package com.dragulaxis;

import java.sql.Connection;
import java.sql.DriverManager;

public class Service {
    public static void main(String[] args) {

    }

    // src\main\resources\db\sqlite\sqliteDB.db
    public static void createDb(String pathAndFileName) {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + pathAndFileName);
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
