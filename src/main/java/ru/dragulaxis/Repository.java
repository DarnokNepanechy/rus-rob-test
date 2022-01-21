package ru.dragulaxis;

import java.sql.*;
import java.util.Locale;

public class Repository {
    private final int column1;
    private final int column2;
    private final int column5;
    private final int column6;
    private final int column7;

    public Repository(int column1, int column2, int column5, int column6, int column7) {
        this.column1 = column1;
        this.column2 = column2;
        this.column5 = column5;
        this.column6 = column6;
        this.column7 = column7;
    }

    public String createSQLRequest(String[] fields) {
        String vendor = fields[column1];
        String number = fields[column2];
        String searchVendor = fields[column1].replaceAll("[^A-Za-z0-9]", "").toUpperCase(Locale.ROOT);
        String searchNumber = fields[column2].replaceAll("[^A-Za-z0-9]", "").toUpperCase(Locale.ROOT);
        String description = fields[column5].length() > 512 ? fields[column5].substring(0, 512) : fields[column5];
        Double price = Double.parseDouble(fields[column6].replace(",", "."));
        int count = Integer.parseInt(fields[column7].replaceAll("[^0-9]", ""));

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
                + "Vendor varchar(64), \n"
                + "Number varchar(64), \n"
                + "SearchVendor varchar(64), \n"
                + "SearchNumber varchar(64), \n"
                + "Description varchar(512), \n"
                + "Price decimal(18,2), \n"
                + "Count int \n"
                + ");";
        try (
                Connection connection = DriverManager.getConnection("jdbc:sqlite:" + "src\\main\\resources\\db\\sqliteDB.db");
                Statement statement = connection.createStatement()
        ) {
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
