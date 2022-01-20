package com.dragulaxis;

import java.io.IOException;
import java.sql.*;
import java.util.Locale;

public class Repository {
    private static final String PATH_TO_DB = "src\\main\\resources\\db\\sqliteDB.db";

    public static void main(String[] args) throws IOException {
        // TODO: создать таблицу по ТЗ
        String sqlCreateTable = "CREATE TABLE IF NOT EXISTS PriceItems (\n"
                + "id integer PRIMARY KEY, \n" // возможно, это поле заменит number
                + "Vendor varchar(64), \n"
                + "Number varchar(64), \n"
                + "SearchVendor varchar(64), \n"
                + "SearchNumber varchar(64), \n"
                + "Description varchar(512), \n"
                + "Price decimal(18,2), \n"
                + "Count int \n"
                + ");";

        String sqlPutData = "INSERT INTO PriceItems (id, Vendor, Number, SearchVendor, SearchNumber, Description, Price, Count) " +
                            "VALUES (1, '555', 'D201-34-350A', '555', 'D20134350A', 'Рычаг подвески | перед лев |', 1343.68, 2);";

        String sqlSelectData = "SELECT * FROM PriceItems;";

        Repository repository = new Repository();
        repository.createDb(PATH_TO_DB);
        repository.sqlRequest(sqlCreateTable);
//        repository.sqlRequest(sqlPutData);
//        repository.selectFromTable(sqlSelectData);


    }

    public String createSQLRequest(int id, String[] fields) {
        String sql = String.format("INSERT INTO PriceItems (id, Vendor, Number, SearchVendor, SearchNumber, Description, Price, Count) " +
                        "VALUES (%d, '%s', '%s', '%s', '%s', '%s', %s, %d);",
                id, fields[1], fields[10],
                fields[1].replaceAll("[^A-Za-z0-9]", "").toUpperCase(Locale.ROOT), fields[10].replaceAll("[^A-Za-z0-9]", "").toUpperCase(Locale.ROOT),
                fields[3], fields[6].replace(',', '.'), Integer.parseInt(fields[8].replaceAll("[^0-9]", "")));
        System.out.println(sql);
        return sql;
    }

    public void sqlRequest(String sql) {
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

            System.out.println("id: " + resultSet.getInt(1));
            System.out.println("Vendor: " + resultSet.getString(2));
            System.out.println("Number: " + resultSet.getString(3));
            System.out.println("SearchVendor: " + resultSet.getString(4));
            System.out.println("SearchNumber: " + resultSet.getString(5));
            System.out.println("Description: " + resultSet.getString(6));
            System.out.println("Price: " + resultSet.getDouble(7));
            System.out.println("Count: " + resultSet.getInt(8));
            System.out.println("==============================\n");


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
