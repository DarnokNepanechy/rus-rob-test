package com.dragulaxis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Service {
    private static final String PATH_TO_DB = "src\\main\\resources\\db\\sqlite\\sqliteDB.db";

    public static void main(String[] args) {

        String sql = "CREATE TABLE IF NOT EXISTS test_table (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	name text NOT NULL,\n"
                + "	capacity real\n"
                + ");";

        try (
                Connection connection = DriverManager.getConnection(PATH_TO_DB);
                Statement statement = connection.createStatement()
        ) {
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createDb(String path) {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + path);
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
