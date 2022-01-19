package com.dragulaxis;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.*;

public class Repository {
    private static final String PATH_TO_DB = "src\\main\\resources\\db\\sqliteDB.db";

    public static void main(String[] args) throws IOException {
        // TODO: создать таблицу по ТЗ
        String sqlCreateTable = "CREATE TABLE IF NOT EXISTS test_table (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	name text NOT NULL,\n"
                + "	capacity real\n"
                + ");";

        String sqlPutData = "INSERT INTO test_table (id, name, capacity) " +
                "VALUES (1, 'name 1', 421);";

        String sqlSelectData = "SELECT * FROM test_table;";

//        Repository repository = new Repository();
//        repository.createDb(PATH_TO_DB);
//        repository.insertInto(sqlPutData);
//        repository.selectFromTable(sqlSelectData);

    }

    public void insertInto(String sql) {
        try (
                Connection connection = DriverManager.getConnection("jdbc:sqlite:" + PATH_TO_DB);
                Statement statement = connection.createStatement()
        ) {
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void selectFromTable(String sql) {
        try (
                Connection connection = DriverManager.getConnection("jdbc:sqlite:" + PATH_TO_DB);
                Statement statement = connection.createStatement()
        ) {
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                double capacity = resultSet.getDouble(3);

                System.out.println("id: " + id);
                System.out.println("Name: " + name);
                System.out.println("Capacity: " + capacity);
                System.out.println("===================\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // TODO: сделать создание БД и директории, если таковых нет
    public void createDb(String path) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + path);
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
