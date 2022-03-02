package ru.dragulaxis;

import java.util.Locale;

class DBClient {

    int column1;
    int column2;
    int column5;
    int column6;
    int column7;

    public DBClient(int column1, int column2, int column5, int column6, int column7) {
        this.column1 = column1;
        this.column2 = column2;
        this.column5 = column5;
        this.column6 = column6;
        this.column7 = column7;
    }

    static String createTableSQL = "CREATE TABLE IF NOT EXISTS PriceItems ("
            + "Vendor varchar(64), "
            + "Number varchar(64), "
            + "SearchVendor varchar(64), "
            + "SearchNumber varchar(64), "
            + "Description varchar(512), "
            + "Price decimal(18,2), "
            + "Count int"
            + ");";

    String createSQLRequest(String[] fields) {
        String vendor = fields[column1];
        String number = fields[column2];
        String searchVendor = fields[column1].replaceAll("[^A-Za-z0-9]", "").toUpperCase(Locale.ROOT);
        String searchNumber = fields[column2].replaceAll("[^A-Za-z0-9]", "").toUpperCase(Locale.ROOT);
        String description = fields[column5].length() > 512 ? fields[column5].substring(0, 512) : fields[column5];
        Double price = Double.parseDouble(fields[column6].replace(",", "."));
        String stringCount = fields[column7];
        if (stringCount.contains("-")) {
            stringCount = stringCount.substring(stringCount.indexOf('-'));
        }
        int count = Integer.parseInt(stringCount.replaceAll("[^0-9]", ""));

        return String.format(
                "INSERT INTO PriceItems " +
                        "(Vendor, Number, SearchVendor, SearchNumber, Description, Price, Count) " +
                        "VALUES " +
                        "('%s', '%s', '%s', '%s', '%s', %s, %d);",
                vendor, number, searchVendor, searchNumber, description, price, count
        );
    }
}
