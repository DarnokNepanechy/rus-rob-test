package ru.dragulaxis;

import java.sql.*;
import java.util.Locale;

public class Repository {
    String PATH_TO_DB = "src\\main\\resources\\db\\sqliteDB.db";

    public String createSQLRequest(String[] fields) {
        String vendor = fields[1].toUpperCase();
        String number = fields[10].toUpperCase();
        String searchVendor = fields[1].replaceAll("[^A-Za-z0-9]", "").toUpperCase(Locale.ROOT);
        String searchNumber = fields[10].replaceAll("[^A-Za-z0-9]", "").toUpperCase(Locale.ROOT);
        String description = fields[3].length() > 512 ? fields[3].substring(0, 512) : fields[3];
        Double price = Double.parseDouble(fields[6].replace(",", "."));
        int count = Integer.parseInt(fields[8].replaceAll("[^0-9]", ""));

        return String.format(
                "INSERT INTO PriceItems " +
                        "(Vendor, Number, SearchVendor, SearchNumber, Description, Price, Count) " +
                "VALUES " +
                        "('%s', '%s', '%s', '%s', '%s', %s, %d);",
                vendor, number, searchVendor, searchNumber, description, price, count
        );
    }

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS PriceItems (\n"
//                + "id integer PRIMARY KEY, \n" // возможно, это поле заменит number
                + "Vendor varchar(64), \n"
                + "Number varchar(64), \n"
                + "SearchVendor varchar(64), \n"
                + "SearchNumber varchar(64), \n"
                + "Description varchar(512), \n"
                + "Price decimal(18,2), \n"
                + "Count int \n"
                + ");";
        try (
                Connection connection = DriverManager.getConnection("jdbc:sqlite:" + PATH_TO_DB);
                Statement statement = connection.createStatement()
        ) {
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createDb() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + PATH_TO_DB);
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
