package ru.dragulaxis;

import com.dragulaxis.PriceItem;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Application {
    static List<PriceItem> priceItemList = new ArrayList<>();

    public static void main(String[] args) throws IOException, CsvException, SQLException {

        // скачивание файла с моей почты
        MailClient client = new MailClient("imap.yandex.ru", 993, "test17012022@yandex.ru", "qyuyjrnkurpzrhrx");
        String filePath = client.downloadCsvFile("Hello World");

        // TODO: поменять на filePath
        File file = new File("C:\\temporary\\file15672310898083658598.csv");

        CSVParser parser = new CSVParserBuilder()
                .withSeparator(';')
                .build();
        CSVReader reader = new CSVReaderBuilder(new FileReader(file))
                .withSkipLines(1)
                .withCSVParser(parser)
                .build();

        String PATH_TO_DB = "src\\main\\resources\\db\\sqliteDB.db";
        Repository repository = new Repository();
        repository.createDb();
        repository.createTable();

        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + PATH_TO_DB);
        connection.setAutoCommit(false);
        Statement statement = connection.createStatement();

        int i = 1;
        try {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                statement.execute(repository.createSQLRequest(nextLine));
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.commit();
            System.out.println("Не удалось обработать строку: " + i);
        }
        connection.commit();
    }
}
