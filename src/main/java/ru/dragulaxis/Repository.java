package ru.dragulaxis;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.sql.*;
import java.util.Locale;

record Repository(int column1, int column2, int column5, int column6, int column7) {
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
        int count = Integer.parseInt(fields[column7].replaceAll("[^0-9]", ""));

        return String.format(
                "INSERT INTO PriceItems " +
                        "(Vendor, Number, SearchVendor, SearchNumber, Description, Price, Count) " +
                        "VALUES " +
                        "('%s', '%s', '%s', '%s', '%s', %s, %d);",
                vendor, number, searchVendor, searchNumber, description, price, count
        );
    }

    void putToDb(String dbPath, String filePath) throws FileNotFoundException, SQLException {
        File file = new File(filePath);

        CSVParser parser = new CSVParserBuilder()
                .withSeparator(';')
                .build();

        CSVReader reader = new CSVReaderBuilder(new FileReader(file))
                .withSkipLines(1)
                .withCSVParser(parser)
                .build();

        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        connection.setAutoCommit(false);
        Statement statement = connection.createStatement();
        statement.execute(createTableSQL);

        int i = 1;
        try {
            String[] nextLine;
            statement.execute("DELETE FROM PriceItems;");
            System.out.println("Загрузка новых данных в базу...");
            while ((nextLine = reader.readNext()) != null) {
                statement.execute(this.createSQLRequest(nextLine));
                i++;
            }
        } catch (CsvValidationException | IOException e) {
            e.printStackTrace();
            System.out.println("Остались непрочитанные данные. Не удалось обработать строку: " + i);
        } finally {
            connection.commit();
            System.out.println("Добавлено " + (i - 1) + " полей");
            connection.close();
            statement.close();
        }
    }
}
